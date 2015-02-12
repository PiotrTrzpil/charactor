package charactor.core.model.persistance

import charactor.misc.Vector2D
import charactor.core.model.objects.charactor.attributes.Attribute

class CharactorSnapshot(val position: Vector2D, val energy: Double, val visibilityRange: Double, val attributes: Traversable[Attribute])
{
}
