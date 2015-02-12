package tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import collection.mutable
import charactor.misc.Vector2D

@RunWith(classOf[JUnitRunner])
class ExampleSuite extends FunSuite
{
   test("pop is invoked on a non-empty stack")
   {

      val stack = new mutable.Stack[Int]
      stack.push(1)
      stack.push(2)
      val oldSize = stack.size
      val result = stack.pop()
      assert(result === 2)
      assert(stack.size === oldSize - 1)
   }

   test("t3")
   {
      val elapsedTime = 50f;
      val velocity = new Vector2D(1f, 1f).normalize * 2f;
      val velocityByTime = velocity * (elapsedTime / 100d);
      val position = new Vector2D(100, 200);
      val result = (position + velocity * (elapsedTime / 100d));
      val result2 = (position + velocityByTime);
     println("position: "+position);
     println("velocityByTime: "+velocityByTime);
     println("(position + velocityByTime): "+(position + velocityByTime));
    //  assert(velocity === 2)
      //assert(velocityByTime === Vector2D(0.705, 0.705));
      assert(result2 === new Vector2D(100.705, 200.705));
      assert(result === new Vector2D(100.705, 200.705));
/*
      val stack = new Charactor();
      val angle = stack.calcAngle(1,0,0.5f,0.5f);
      val angle2 = stack.calcAngle(0.5f,0.5f,0,1);
      val angle3 = stack.calcAngle(0.8f,0.2f,0,1);
      val angle4 = stack.calcAngle(0.2f,0.8f,0,1);
      val angle5 = stack.calcAngle(0,1,0.2f,0.8f);
      val angle6 = stack.calcAngle(0,1,-0.5f,-0.5f);

      val vec1 = stack.calcVector(0f);
      val vec2 = stack.calcVector(30f);
      val vec3 = stack.calcVector(45f);
      val vec4 = stack.calcVector(90f);
      val vec5 = stack.calcVector(135f);
      val vec6 = stack.calcVector(180f);
      val vec7 = stack.calcVector(270f);
      val vec8 = stack.calcVector(360f);
      val vec9 = stack.calcVector(360f);



      assert(angle > 0);*/
   }
}