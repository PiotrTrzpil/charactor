package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.charactor.parts.MoveDecider
import charactor.core.model.objects.charactor.Charactor
import actors.OutputChannel
import charactor.core.model.objects.Positionable
import charactor.core.messages.{MoveMessage, MoveAnywhereMessage}

class BlindMoveDecider extends MoveDecider
{
	override def decide(owner: Charactor, sender: OutputChannel[Any], nearest: List[(Positionable, Double)], timeScale: Double) =
	{
		if (nearest.isEmpty)
			sender ! new MoveAnywhereMessage(owner.speed.Value*timeScale);
		else
		{
			val target = nearest.minBy(_._2)._1;
			sender ! new MoveMessage(target, owner.speed.Value*timeScale);
		}
	}
}

class BlindAttribute extends Attribute(List(new BlindMoveDecider))
{

}
