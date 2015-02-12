package charactor.core.model.objects.charactor.parts

import charactor.core.model.objects.charactor.Charactor
import actors.OutputChannel
import charactor.core.model.objects.{FoodSource, Positionable}
import charactor.core.messages.ConsumeFoodMessage

object CollisionDecider
{

}

class CollisionDecider
{
	def decide(owner: Charactor, sender: OutputChannel[Any], colliding: List[Positionable], timeScale: Double) =
	{
		val foodSources = colliding.filter(item => item.isInstanceOf[FoodSource]);
		val charactors = colliding.filter(item => item.isInstanceOf[Charactor]);

		if (!charactors.isEmpty)
			owner.fightStrategy.fight(owner, charactors(0).asInstanceOf[Charactor]);
		else
			foodSources(0).asInstanceOf[FoodSource] ! new ConsumeFoodMessage(owner.energy.lackingAmount());
	}
}
