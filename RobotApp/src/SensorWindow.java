import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;

public class SensorWindow extends GLFW{
	//Use glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
	public final static int WINDOW_HEIGHT = 700;
	public final static int WINDOW_WIDTH = 700;
	static ArrayList<Line> lines = new ArrayList<Line>();
	
	static double targetFPMS = 1000000000/30;
	static Texture texture;

	public static void createWindow() {
		double start = System.nanoTime();
		System.out.println("Intializing window");
		glfwInit();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		GLFWVidMode monitor = glfwGetVideoMode(glfwGetPrimaryMonitor());
		long window = glfwCreateWindow(WINDOW_WIDTH,WINDOW_HEIGHT, "Isometric game",0,0);
		glfwSetWindowPos(window, monitor.width()/2-WINDOW_WIDTH/2,monitor.height()/2-WINDOW_HEIGHT/2);
		glfwShowWindow(window);
		System.out.println("Setting up graphics");
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0,0,0,0);


		texture = new Texture("texture.png");

		System.out.println("Done!\n"+ (System.nanoTime()-start)*0.000001+" ms");
		double timeFPS = System.nanoTime();
		double currentFPS = (System.nanoTime()-timeFPS);
		for(int i =0; i < 360; i++) {
			lines.add(new Line(i, i%8));
		}
		while(!glfwWindowShouldClose(window)) {

			currentFPS = (System.nanoTime()-timeFPS);	
			if(currentFPS >= targetFPMS) {
				timeFPS = System.nanoTime();
				glfwPollEvents();
				glClear(GL_COLOR_BUFFER_BIT);
				
				glColor3f(255,255,255);
				fillRect(-0.5f,0.5f,1,1,texture,0);
				
				for(int i =0; i < lines.size(); i++) {
					glColor3f(lines.get(i).getQuality()[0],lines.get(i).getQuality()[1],0);
					lines.get(i).draw();
					lines.get(i).decay(1);
					if(lines.get(i).isDead())
						lines.remove(i);
				}
				
				glfwSwapBuffers(window);
			}
		}	
		glfwTerminate();
	}
	public static void drawLine(float x1, float y1, float x2, float y2) {
		
		glBindTexture(GL_TEXTURE_2D, 0);
		glBegin(GL_LINES);
		glVertex2f(x1,y1);
		glVertex2f(x2,y2);
		glEnd();
	}
	public static void fillRect(float x, float y, float width, float height, Texture texture, int angle) {
		glPushMatrix();	
		glTranslatef(x+width/2,y-height/2,0);
		glRotatef(angle, 0,0, 1);
		glTranslatef(-(x+width/2),-(y-height/2),0);

		texture.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(x,y);

		glTexCoord2f(1,0);
		glVertex2f(x+width, y);

		glTexCoord2f(1,1);
		glVertex2f(x+width, y-height);

		glTexCoord2f(0,1);
		glVertex2f(x, y-height);
		glEnd();

		glPopMatrix();
	}

	public static void fillBlock(float x, float y, float width, float length, float height ,Texture texture) {

		texture.bind();
		glBegin(GL_POLYGON);
		glTexCoord2f(0.5f,0);
		glVertex2f(x,y);

		glTexCoord2f(1,0.25f);
		glVertex2f(x+width/2, y-length/2);

		glTexCoord2f(1,0.75f);
		glVertex2f(x+width/2, y-length/2-height);

		glTexCoord2f(0.5f,1);
		glVertex2f(x, y-length-height);

		glTexCoord2f(0,0.75f);
		glVertex2f(x-width/2, y-length/2-height);
		glTexCoord2f(0,0.25f);

		glVertex2f(x-width/2, y-length/2);

		glEnd();



	}

	public static void drawTriangle(float x1, float y1, float x2,float y2, float x3, float y3) {
		glBindTexture(GL_TEXTURE_2D, 0);

		glBegin(GL_TRIANGLES);
		glVertex2f(x1,y1);
		glVertex2f(x2, y2);
		glVertex2f(x3,y3);
		glEnd();
		glColor3f(255,255,255);
	}
	public static void drawRect(float x, float y, float width, float height) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBegin(GL_LINE_STRIP);
		glVertex2f(x,y);

		glVertex2f(x+width, y);

		glVertex2f(x+width, y-height);

		glVertex2f(x, y-height);
		glVertex2f(x,y);
		glEnd();
	}
	public static void main(String[] args) {
		createWindow();
	}



}


