package charactor.core.model.objects.charactor

import _root_.objects.core.model.WorldMap
import actors.Actor

import _root_.charactor.core.messages._
import charactor.core.model.objects.charactor.attributes.Attribute
import _root_.charactor.core.model._
import objects.{Energy, Moveable}
import parts._

object Charactor
{
	private val EnergyDistanceConsumptionRatio = 0.1d;
	private val EnergyDecayRatio = 0.4d;
}

class Charactor(private val map: WorldMap, val attributes: Traversable[Attribute]) extends Actor with Moveable
{
	def this(mapParam: WorldMap, attributesParam: List[Attribute]) =
	{
		this(mapParam, attributesParam.asInstanceOf[Traversable[Attribute]]);

		attributesParam.foreach(attribute =>
		{
			attribute.modifiers.foreach(modifier =>
			{
				modifier match
				{
					case e: Energy => energy = e;
					case s: Speed => speed = s;
					case v: VisibilityRange => visibilityRange = v;
					case r: Resistance => resistance = r;
					case m: MoveDecider => moveDecider = m;
					case a: ActionDecider => actionDecider = a;
					case c: CollisionDecider => collisionDecider = c;
					case f: FightStrategy => fightStrategy = f;
				}
			});
		});
	}

  var name = "";

	protected[charactor] var energy: Energy = new Energy(Energy.DefaultMaximum);
	protected[charactor] var speed: Speed = new LinearSpeed(Speed.DefaultMaximumSpeed, Speed.DefaultMinimumSpeed);
	protected[charactor] var visibilityRange: VisibilityRange = new VisibilityRange(VisibilityRange.DefaultRange);
	protected[charactor] var resistance: Resistance = new Resistance(Resistance.DefaultValue);
	protected[charactor] var actionDecider: ActionDecider = new ActionDecider();
	protected[charactor] var moveDecider: MoveDecider = new MoveDecider();
	protected[charactor] var collisionDecider: CollisionDecider = new CollisionDecider();
	protected[charactor] var fightStrategy: FightStrategy = new FightStrategy();

  override def toString() =  name;

	def act()
	{
		loop
		{
			react
			{
				case surrounding: SurroundingMessage =>
				{
					consumeEnergy(Charactor.EnergyDecayRatio*surrounding.timeScale);
					actionDecider.decide(this, sender, surrounding.nearest, surrounding.colliding, surrounding.timeScale);
				}
				case report: MoveReportMessage =>
				{
					consumeEnergy(report.movedDistance*Charactor.EnergyDistanceConsumptionRatio);
				}
				case message: CreateSnapshotMessage =>
				{
					reply {new CharactorStateMessage(energy.Value, visibilityRange.range, attributes)};
				}
				case report: ConsumeReportMessage =>
				{
					restoreEnergy(report.consumedAmount);
				}
				case energyComparer: EnergyComparisonMessage =>
				{
					sender ! new EnergyComparisonResultMessage(energyComparer.valueToBeGreater > energy.Value, energyComparer.timeScale);
				}
				case energyComparisonResult: EnergyComparisonResultMessage =>
				{
					if (energyComparisonResult.isGreater)
						consumeEnergy(Charactor.EnergyDecayRatio*energyComparisonResult.timeScale);
					else
						restoreEnergy(Charactor.EnergyDecayRatio*energyComparisonResult.timeScale);
				}
				case attack: AttackMessage =>
				{
					consumeEnergy(attack.damage/resistance.damageModifier);
					if (energy.isDepleted())
						map ! new DroppedDeadMessage(this);
					else
						fightStrategy.fight(this, sender);
				}
				case visibility: ProvideVisibilityRangeMessage =>
				{
					sender ! new VisibilityRangeMessage(visibilityRange.range, visibility.timeScale);
				}
        case x : Any => println("WARNING: "+this+" received unexpected message: "+x);
			}
		}
	}

   private def logMessageReceived(message:Any)
   {
      println("DEBUG: "+this+" received "+message+" from "+sender);
   }

	protected[charactor] def restoreEnergy(amount: Double)
	{
		energy.restore(amount);
    speed.compute(List(energy));
	}

	protected[charactor] def consumeEnergy(amount: Double)
	{
		energy.consume(amount);
		speed.compute(List(energy))
	}
}
