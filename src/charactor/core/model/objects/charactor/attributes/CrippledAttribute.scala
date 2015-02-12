package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.charactor.{Speed, LinearSpeed}

object CrippledAttribute
{
	val MaximumSpeedModifier = 0.7;
}

class CrippledAttribute extends Attribute(List(new LinearSpeed(Speed.DefaultMaximumSpeed*CrippledAttribute.MaximumSpeedModifier, Speed.DefaultMinimumSpeed)))
{
}
