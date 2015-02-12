package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.charactor.Resistance

object ToughAttribute
{
	val DamageModifier = 1.2d;
}

class ToughAttribute extends Attribute(List(new Resistance(ToughAttribute.DamageModifier)))
{

}
