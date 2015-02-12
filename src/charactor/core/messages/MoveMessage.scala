package charactor.core.messages

import charactor.core.model.objects.Positionable

class MoveMessage(val target: Positionable, val speed: Double)
{
}
