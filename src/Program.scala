import charactor.core.control.MainController
import charactor.core.visualization.GamePanel

import swing._

object Program extends SwingApplication
{
   private val mainPanel = new GamePanel
   private val controller = new MainController(mainPanel)

   private def top = new MainFrame
   {
      title = "Charactor Simulation"
      contents = mainPanel;
      size = new Dimension(1010, 610)
   }

   override def startup(args: Array[String])
   {
     controller.start();
	   mainPanel.beginDrawing();

      val t = top;

      t.pack();
      t.visible = true;
   }
}