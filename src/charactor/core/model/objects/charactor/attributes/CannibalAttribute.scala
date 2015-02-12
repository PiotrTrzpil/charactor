package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.charactor.parts.FightStrategy
import charactor.core.model.objects.charactor.Charactor
import actors.OutputChannel

class CannibalFightStrategy extends FightStrategy
{
	override def fight(owner: Charactor, target: OutputChannel[Any])
	{
		super.fight(owner, target);
		owner.restoreEnergy(CannibalAttribute.RestoredEnergy);
	}
}

object CannibalAttribute
{
	val RestoredEnergy = 20;
}

class CannibalAttribute extends Attribute(List())
{

}
