package charactor.core.model.objects.charactor.parts

import actors.OutputChannel
import charactor.core.model.objects.Positionable
import charactor.core.model.objects.charactor.Charactor

object ActionDecider
{

}

class ActionDecider
{
	def decide(owner: Charactor, sender: OutputChannel[Any], nearest: List[(Positionable, Double)], colliding: List[Positionable], timeScale: Double) =
	{
		if (colliding.isEmpty)
			owner.moveDecider.decide(owner, sender, nearest, timeScale);
		else
			owner.collisionDecider.decide(owner, sender, colliding, timeScale);
	}
}
