package charactor.core.model.objects.charactor

object Speed
{
	val ExhaustedSpeedModifier = 0.5d;
	val DefaultMaximumSpeed = 4d;
	val DefaultMinimumSpeed = 2d;
}

abstract class Speed(val maximum: Double, val minimum: Double)
{
	protected var value = maximum;

	def compute(context: Iterable[Any]);

	def Value(): Double = value;
	def range(): Double = (maximum - minimum);
}
