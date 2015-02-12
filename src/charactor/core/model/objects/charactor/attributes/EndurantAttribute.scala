package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.Energy

object EndurantAttribute
{
	val MaximumEnergy = 140d;
}

class EndurantAttribute extends Attribute(List(new Energy(EndurantAttribute.MaximumEnergy)))
{
}
