package charactor.core.model.objects.charactor.parts

import charactor.core.model.objects.charactor.Charactor
import charactor.core.messages.AttackMessage
import actors.OutputChannel

object FightStrategy
{

}

class FightStrategy
{
	def fight(owner: Charactor, target: OutputChannel[Any])
	{
		target ! new AttackMessage(owner.energy.Value);
	}
}
