package charactor.core.model.objects.charactor

import charactor.core.model.objects.Energy

class ReversedLinearSpeed(maximumParam: Double, minimumParam: Double) extends Speed(maximumParam, minimumParam)
{
	def compute(context: Iterable[Any]) =
	{
		val energySet = context.filter(item => item.isInstanceOf[Energy]);
		if (energySet.size != 1)
			value = 0;
		else
		{
			val energy = energySet.iterator.next().asInstanceOf[Energy];
			value = minimum + (1 - energy.fraction)*range;
			if (energy.isDepleted())
				value *= Speed.ExhaustedSpeedModifier;
		};
	}
}
