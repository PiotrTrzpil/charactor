package charactor.core.messages

import charactor.core.model.objects.Positionable

class SurroundingMessage(val nearest: List[(Positionable, Double)], val colliding: List[Positionable], val timeScale: Double)
{
}
