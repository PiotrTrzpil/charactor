package charactor.core.model.objects.charactor.parts

import charactor.core.messages.{MoveMessage, MoveAnywhereMessage}
import charactor.core.model.objects.{FoodSource, Positionable}
import actors.OutputChannel
import charactor.core.model.objects.charactor.Charactor

object MoveDecider
{

}

class MoveDecider
{
	def decide(owner: Charactor, sender: OutputChannel[Any], nearest: List[(Positionable, Double)], timeScale: Double) =
	{
		if (nearest.isEmpty)
			sender ! new MoveAnywhereMessage(owner.speed.Value*timeScale);

		val foodSources = nearest.filter(pair => pair._1.isInstanceOf[FoodSource]);
		val charactors = nearest.filter(pair => pair._1.isInstanceOf[Charactor]);

		if (foodSources.isEmpty & charactors.isEmpty)
			sender ! new MoveAnywhereMessage(owner.speed.Value*timeScale);
		else
		{
			val target = if(!foodSources.isEmpty) foodSources.minBy(_._2)._1 else charactors.minBy(_._2)._1;

			sender ! new MoveMessage(target, owner.speed.Value*timeScale);
		}
	}
}
