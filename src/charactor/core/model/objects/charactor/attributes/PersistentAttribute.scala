package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.charactor.{ReversedLinearSpeed, Speed}

object PersistentAttribute
{
	val MaximumSpeedModifier = 1.6;
	val MinimumSpeedModifier = 1.4;
}

class PersistentAttribute extends Attribute(List(new ReversedLinearSpeed(Speed.DefaultMaximumSpeed*PersistentAttribute.MaximumSpeedModifier, Speed.DefaultMinimumSpeed*PersistentAttribute.MinimumSpeedModifier)))
{
}
