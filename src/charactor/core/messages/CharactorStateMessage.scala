package charactor.core.messages

import charactor.core.model.objects.charactor.attributes.Attribute

class CharactorStateMessage(val energy: Double, val visibilityRange: Double, val attributes: Traversable[Attribute])
{
}
