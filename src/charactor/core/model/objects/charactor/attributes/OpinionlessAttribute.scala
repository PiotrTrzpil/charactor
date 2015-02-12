package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.charactor.parts.ActionDecider
import charactor.core.model.objects.charactor.Charactor
import actors.{Actor, OutputChannel}
import charactor.core.model.objects.Positionable
import charactor.core.messages.EnergyComparisonMessage

class OpinionlessActionDecider extends ActionDecider
{
	override def decide(owner: Charactor, sender: OutputChannel[Any], nearest: List[(Positionable, Double)], colliding: List[Positionable], timeScale: Double) =
	{
		nearest.filter(pair => pair._1.isInstanceOf[Charactor]).foreach(near => near._1.asInstanceOf[Actor] ! new EnergyComparisonMessage(owner.energy.Value, timeScale));

		super.decide(owner, sender, nearest, colliding, timeScale);
	}
}

class OpinionlessAttribute extends Attribute(List())
{

}
