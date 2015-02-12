package charactor.core.model.objects.charactor.attributes

import charactor.core.model.objects.charactor.parts.{MoveDecider, CollisionDecider}
import charactor.core.model.objects.charactor.Charactor
import actors.OutputChannel
import charactor.core.model.objects.Positionable
import charactor.core.messages.{MoveMessage, MoveAnywhereMessage}

class UndeadCollisionDecider extends CollisionDecider
{
	override def decide(owner: Charactor, sender: OutputChannel[Any], colliding: List[Positionable], timeScale: Double) =
	{
		val charactors = colliding.filter(item => item.isInstanceOf[Charactor]);

		if (!charactors.isEmpty)
			owner.fightStrategy.fight(owner, charactors(0).asInstanceOf[Charactor]);
	}
}

class UndeadMoveDecider extends MoveDecider
{
	override def decide(owner: Charactor, sender: OutputChannel[Any], nearest: List[(Positionable, Double)], timeScale: Double) =
	{
		if (nearest.isEmpty)
			sender ! new MoveAnywhereMessage(owner.speed.Value*timeScale);

		val charactors = nearest.filter(pair => pair._1.isInstanceOf[Charactor]);

		if (charactors.isEmpty)
			sender ! new MoveAnywhereMessage(owner.speed.Value*timeScale);
		else
		{
			val target = charactors.minBy(_._2)._1;
			sender ! new MoveMessage(target, owner.speed.Value*timeScale);
		}
	}
}

class UndeadAttribute extends Attribute(List(new UndeadCollisionDecider, new UndeadMoveDecider))
{
}
