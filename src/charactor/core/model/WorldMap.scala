package objects.core.model

import collection.mutable.ListBuffer
import collection.mutable.Map
import scala.actors.Actor

import util.Random

import charactor.core.model.objects.{Positionable, FoodSource, Moveable}
import charactor.core.control.MainController
import charactor.core.model.objects.charactor.Charactor
import charactor.core.messages._
import charactor.core.model.persistance._
import charactor.misc.Vector2D
import charactor.core.model.objects.charactor.attributes._

object WorldMap
{
	val CollisionRange = 2d;
}

class WorldMap(private val controller: MainController, private val worldBounds: Vector2D) extends Actor
{
	def this(controllerParam: MainController, worldBoundsParam: Vector2D, population: Integer, foods: Integer) =
	{
		this(controllerParam, worldBoundsParam);

		for (i<-0 until population)
			addCharactor();

		for (i<-0 until foods)
			addFoodSource();
	}

	private val randomizer = Random;
	private val movable: Map[Moveable, MoveableState] = Map();
	private val positions: Map[Positionable, PositionableState] = Map();
	private val charactors: ListBuffer[Charactor] = ListBuffer();
	private val foodSources: ListBuffer[FoodSource] = ListBuffer();

  override def toString() =  "WorldMap";


  private def logMessageReceived(message:Any)
  {
    println("DEBUG: "+this+" received "+message+" from "+sender);
  }
	def act()
	{
		charactors.foreach(charactor => charactor.start());
		foodSources.foreach(source => source.start());

		loop
		{
			react
			{
				case move: MoveMessage =>
				{
					val movingObject = sender.asInstanceOf[Charactor];
					if (charactors.contains(movingObject) && (positions.contains(move.target)))
						sender ! new MoveReportMessage(performMove(movable(movingObject), positions(move.target), move.speed));
				}
				case move: MoveAnywhereMessage =>
				{
					val movingObject = sender.asInstanceOf[Charactor];
					if (charactors.contains(movingObject) & charactors.size > 1)
						sender ! new MoveReportMessage(performMove(movable(movingObject), anyTargetBut(movingObject), move.speed));
				}
				case time: TimePassMessage =>
				{
					pollDecisions(time.scale);
					growFoodSources(time.scale);
				}
				case message: CreateSnapshotMessage =>
				{
					val charactorsSnapshots = new ListBuffer[CharactorSnapshot];
					charactors.foreach(charactor =>
					{
						(charactor !? new CreateSnapshotMessage) match
						{
							case state: CharactorStateMessage	=> charactorsSnapshots.append(constructCharactorSnapshot(charactor, state));
						}
					});

					val foodSourcesSnapshots = new ListBuffer[FoodSourceSnapshot];
					foodSources.foreach(source =>
					{
						(source !? new CreateSnapshotMessage) match
						{
							case state: FoodSourceStateMessage	=> foodSourcesSnapshots.append(constructFoodSourceSnapshot(source, state));
						}
					});
					reply {new SnapshotMessage(charactorsSnapshots.toList, foodSourcesSnapshots.toList)};
				}
				case dead: DroppedDeadMessage =>
				{
					dead.obj match
					{
						case source: FoodSource =>
						{
							removeFoodSource(source);
						}
						case charactor: Charactor =>
						{
							removeCharactor(charactor);
						}
					}
				}
				case visibility: VisibilityRangeMessage =>
				{
					sender ! specifyCharactorSurrounding(sender.asInstanceOf[Charactor], visibility.visibilityRange, visibility.timeScale)
				}
        case x : Any => println("WARNING: unexpected message: "+x);
			}
		}
	}

	private def performMove(moveable: MoveableState, target: PositionableState, speed: Double): Double =
	{
		var distance = moveable.moveToward(target, speed);
		val collisions = positions.filter(pair => pair._2.position.distanceFrom(moveable.position) <= WorldMap.CollisionRange);
		if (!collisions.isEmpty)
		{
			val collisionAligment = collisions.minBy(pair => pair._2.position.distanceFrom(moveable.position));
			distance += moveable.position.distanceFrom(collisionAligment._2.position);
			moveable.position = collisionAligment._2.position;
		}

		return distance;
	}

	private def constructCharactorSnapshot(charactor: Charactor, state: CharactorStateMessage): CharactorSnapshot =
	{
		return new CharactorSnapshot(positions(charactor).position, state.energy, state.visibilityRange, state.attributes);
	}

	private def constructFoodSourceSnapshot(source: FoodSource, state: FoodSourceStateMessage): FoodSourceSnapshot =
	{
		return new FoodSourceSnapshot(positions(source).position, state.supply);
	}

	private def pollDecisions(timeScale: Double)
	{
		charactors.foreach(charactor => charactor ! new ProvideVisibilityRangeMessage(timeScale));
	}

	private def growFoodSources(timeScale: Double)
	{
		foodSources.foreach(source => source ! new TimePassMessage(timeScale));
	}

	private def specifyCharactorSurrounding(charactor: Charactor, visibilityRange: Double, timeScale: Double): SurroundingMessage =
	{
		if (positions.contains(charactor))
		{
			val pos = positions(charactor);
			val nearest = positions.map(pair => (pair._1, (positions(pair._1).position.distanceFrom(pos.position))))
	      .filter(pair => (pair._2 <= visibilityRange) && (pair._1 != charactor));
			val colliding = nearest.filter(pair => (pair._2 <= WorldMap.CollisionRange)).map(pair => pair._1);
			colliding.foreach(item => nearest.remove(item));

			return new SurroundingMessage(nearest.toList, colliding.toList, timeScale);
		}
		else
			return new SurroundingMessage(List.empty, List.empty, timeScale);
	}

	private def generateRandomPosition(): Vector2D =
	{
		return new Vector2D(randomizer.nextDouble*worldBounds.x, randomizer.nextDouble*worldBounds.y);

	}

   def generateRandomAttributes() : List[Attribute] =
   {
     val list = new ListBuffer[List[Attribute]];
	   list.append(List(new ToughAttribute, new BlindAttribute));
	   list.append(List(new EndurantAttribute, new CrippledAttribute));
	   list.append(List(new PersistentAttribute, new OpinionlessAttribute));
	   list.append(List(new EagleEyeAttribute, new OpinionlessAttribute));
	   list.append(List(new CannibalAttribute, new UndeadAttribute));
     return list(Random.nextInt(list.size));
   }

   private def addCharactor() =
	{
    val position = generateRandomPosition();
    val attributes = generateRandomAttributes();
    val charactor = new Charactor(this, generateRandomAttributes());
    charactor.name = "Char"+charactors.size;
    val state = new MoveableState(position, worldBounds);
    charactors.append(charactor);
		movable.put(charactor, state);
		positions.put(charactor, state);
  }

	private def addFoodSource()
	{
		val position = generateRandomPosition();
		val supply = math.max(FoodSource.Minimum + 1, randomizer.nextInt(FoodSource.Maximum.toInt));
		val source = new FoodSource(this, supply);
    source.name = "Food"+foodSources.size;
		val state = new PositionableState(position, worldBounds);
		foodSources.append(source);
		positions.put(source, state);
	}

	private def removeFoodSource(source: FoodSource)
	{
		if (foodSources.contains(source))
		{
			foodSources -= source;
			positions -= source;
			source.trapExit;
		}
	}

  private def removeCharactor(char: Charactor) =
  {
	  if (charactors.contains(char))
	  {
		  charactors -= char;
		  movable -= char;
		  positions -= char;
		  char.trapExit;
	  }
  }

	private def anyTargetBut(but: Positionable): PositionableState =
  {
		val available = positions.keys.toList.diff(List(but));
    return positions(available(randomizer.nextInt(available.size)));
  }
}
