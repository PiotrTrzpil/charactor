package charactor.core.model.objects

import _root_.charactor.core.messages._
import actors.Actor
import objects.core.model.WorldMap

object FoodSource
{
	val Maximum = 50d;
	val Minimum = 0d;

	private val DefaultGrowthRatio = 0.1d;
}

class FoodSource(mapParam: WorldMap, supplyParam: Double) extends Actor with Positionable
{
	private val map: WorldMap = mapParam;
	private var supply: Double = Math.max(FoodSource.Minimum + 1, Math.min(FoodSource.Maximum, supplyParam));
	private var growthRatio = FoodSource.DefaultGrowthRatio;

  var name = "";


  override def toString() =  name;

	def act()
	{
		loop
		{
			react
			{
				case consume: ConsumeFoodMessage =>
				{
					val afterConsumptiont = Math.max(FoodSource.Minimum, supply - consume.amount);
					sender ! new ConsumeReportMessage(supply - afterConsumptiont);
					supply = afterConsumptiont;

					if (supply == FoodSource.Minimum)
						map ! new DroppedDeadMessage(this);
				}
				case time: TimePassMessage =>
				{
					supply = Math.min(FoodSource.Maximum, supply + growthRatio*time.scale);
				}
				case message: CreateSnapshotMessage =>
				{
					  reply {new FoodSourceStateMessage(supply)};
				}
        case x : Any => println("WARNING: unexpected message: "+x);
			}
		}
	}
}
