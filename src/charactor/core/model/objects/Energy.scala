package charactor.core.model.objects

object Energy
{
	val DefaultMaximum = 100d;
}

class Energy(maximumParam: Double)
{
	private var value = maximumParam;

   val maximum = maximumParam;
   val minimum = 0d;

	def consume(amount: Double)
	{
		value = math.max(minimum, value - amount);
	}

	def restore(amount: Double)
	{
		value = math.min(maximum, value + amount);
	}

	def isDepleted(): Boolean = (value == minimum);
	def lackingAmount(): Double = (maximum - value);
	def range(): Double = (maximum - minimum);
	def fraction(): Double = ((value - minimum)/range);

	// In Scala, you cant define property with different visibility of getter and setter (Hip Hip WTF),
	// so here is SECOND getter with name in Pascal convention
	def Value(): Double = value;
}
