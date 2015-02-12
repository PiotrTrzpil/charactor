package charactor.core.visualization

import swing.Panel
import java.awt.{Color, Dimension, Graphics2D}
import javax.swing.SwingUtilities
import charactor.core.model.persistance.WorldMapSnapshot
import java.util.concurrent.{Semaphore, LinkedBlockingQueue, BlockingQueue}
import charactor.core.model.objects.FoodSource

class GamePanel extends Panel
{
		def width = 1000;
		def height = 600;

   preferredSize = new Dimension(width, height);

	private var snapshots: BlockingQueue[WorldMapSnapshot] = new LinkedBlockingQueue[WorldMapSnapshot];
	private var snapshot: WorldMapSnapshot = null;
	private val semaphore = new Semaphore(1);

   override def paintComponent(g:Graphics2D)
   {
      super.paintComponent(g);

	    if (snapshot.message == null)
	    {
		    g.setColor(Color.BLACK);
		    g.fillRect(0, 0, width, height);
	      snapshot.charactorsState.foreach(state  =>
	      {
		      val x = state.position.x.toInt;
		      val y = state.position.y.toInt;
		      val r = state.visibilityRange.toInt;
		      g.setColor(Color.WHITE);
		      g.fillOval(x - 2, y - 2, 4, 4);
		      g.setColor(Color.GREEN);
		      g.drawString(state.energy.toInt.toString, x - 4, y - 3);
		      g.setColor(Color.ORANGE);
		      g.drawOval(x - r/2, y - r/2, r, r);
	      });
		   snapshot.foodSourcesState.foreach(state =>
		   {
			   val x = state.position.x.toInt;
			   val y = state.position.y.toInt;
			   g.setColor(Color.red);
			   val size = ((FoodSource.Maximum/10)*(state.supply/FoodSource.Maximum) + 4).toInt;
			   g.fillOval(x - size/4, y - size/4, size/2, size/2);
			   g.setColor(Color.GREEN);
			   g.drawString(state.supply.toInt.toString, x - 4, y - 3);
		   });
	    }
	   else
	    {
		    g.setColor(Color.BLACK);
		    g.fillRect(0, 0, width, height);
		    g.setColor(Color.WHITE);
		    g.drawString(snapshot.message, 10, 10);
	    }
   }

   def beginDrawing()
   {
	   new Thread(new Runnable
	   {
		   def run()
		   {
			   while (true)
			   {
				   semaphore.acquire();
				   snapshot = snapshots.take();
				   SwingUtilities.invokeLater(new Runnable()
				   {
					   def run()
					   {
						   GamePanel.this.repaint();
						   semaphore.release();
					   }
				   });
			   }
		   }
	   }).start();
   }

	def queueSnapshot(snapshot: WorldMapSnapshot) = snapshots.put(snapshot);
}
