package charactor.misc


/**
 * Created with IntelliJ IDEA.
 * User: SysOp
 * Date: 1/19/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
object ExtensionMethods
{


  class RichTraversableLike[T]( trav: List[T] )
  {
    def filter[TClass](classs : Class[TClass]) : List[TClass] =
    {
       trav.filter(_.getClass eq classs).map(_.asInstanceOf[TClass]);



    }

  }
  implicit def richTraversableLike[T]( i: List[T] ) = new RichTraversableLike[T]( i )



}
