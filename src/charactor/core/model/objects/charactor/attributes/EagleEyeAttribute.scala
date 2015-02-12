package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.charactor.VisibilityRange

object EagleEyeAttribute
{
	val VisibilityRange = 300d;
}

class EagleEyeAttribute extends Attribute(List(new VisibilityRange(EagleEyeAttribute.VisibilityRange)))
{

}
