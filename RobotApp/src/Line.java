
public class Line {
	private double length = 0.5;
	private double angle = 30;
	private double life = 30;
	private float x, y;
	private float quality;

	public Line(double angle, int quality) {
		this.angle = Math.toRadians(angle);
		x = (float)(length*Math.cos(this.angle));
		y= (float)(length*Math.sin(this.angle));
		this.quality = quality;
	}
	public void draw() {
		SensorWindow.drawLine(0,0,x,y);
	}
	public void decay(double n) {
		life-= n;
	}
	public boolean isDead() {
		return (life <= 0);
	}
	public float[] getQuality() {
		float standard = 8;
		if(quality < standard) {

			float r;
			float g;
			
			if(quality == 0) {
				r = 255;
				g = 0;
			}
			else {
				r = 255*quality/standard;
				g = 255 - 255*quality/standard;
			}
			float[] arr = {r,g};
			return arr;
		}
		float[] arr ={0,255};
		return arr;
	}
}
