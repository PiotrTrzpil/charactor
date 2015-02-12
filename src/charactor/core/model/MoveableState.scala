package objects.core.model

import charactor.misc.Vector2D

class MoveableState(positionParam: Vector2D, boundariesParam: Vector2D) extends PositionableState(positionParam, boundariesParam)
{
  def moveToward(target: PositionableState, speed: Double): Double =
  {
    assert(target ne this);

    val direction = (target.position - position).normalize;
	  val path = (direction*speed).min(target.position);
    position = (position + path).min(boundaries).max(0);


	  return path.length;
  }
}