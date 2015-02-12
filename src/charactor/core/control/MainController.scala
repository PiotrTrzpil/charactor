package charactor.core.control

import actors.{TIMEOUT, Actor}

import charactor.misc.Vector2D
import charactor.core.visualization.GamePanel
import charactor.core.messages.{SnapshotMessage, CreateSnapshotMessage, TimePassMessage}
import charactor.core.model.persistance.WorldMapSnapshot
import objects.core.model.WorldMap

object MainController
{
	private val DefaultPopulationSize = 40;
	private val DefaultFoodSourceCount = 60;
	private val DefaultUpdateInterval = 33d;
}

class MainController(panel: GamePanel) extends Actor
{
	private val worldMap = new WorldMap(this, new Vector2D(panel.width, panel.height), MainController.DefaultPopulationSize, MainController.DefaultFoodSourceCount);

	private var updateInterval = MainController.DefaultUpdateInterval;
	private var previousTime = System.currentTimeMillis.toDouble;
	private var restTime = MainController.DefaultUpdateInterval;

  def act()
  {
		worldMap.start();

	  loop
	  {
		  reactWithin(restTime.toInt)
		  {
			  case TIMEOUT =>
				{
				  val beforTime = System.currentTimeMillis;
					val snapshot = new WorldMapSnapshot();
					(worldMap !? new CreateSnapshotMessage()) match
			    {
			      case snapshotMessage: SnapshotMessage =>
			      {
			        snapshot.charactorsState = snapshotMessage.charactorsState;
			        snapshot.foodSourcesState = snapshotMessage.foodSourcesState;
				      if (snapshot.charactorsState.size == 1)
					      snapshot.message = snapshot.charactorsState(0).attributes.toString();
			      };
			    }
			    panel.queueSnapshot(snapshot);

			    worldMap ! new TimePassMessage((System.currentTimeMillis.toDouble - previousTime)/updateInterval);
				  previousTime = System.currentTimeMillis.toDouble;
				  val spentTime = System.currentTimeMillis - beforTime;
				  restTime = math.max(0, updateInterval.toInt - spentTime);
				}
		  }
	  }
  }
}
