package com.boruili.path_nav;

import java.util.*;

public class LineSimplification {

	public static void main(String[] args) {
		// quick test on RDP Algorithm
		Vector2 p1 = new Vector2(0,0);
		Vector2 p2 = new Vector2(3,4);
		Vector2 p3 = new Vector2(5,5);
		ArrayList<Vector2> points = new ArrayList<Vector2>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		ArrayList<Vector2> result = DouglasPeucker(points, 0, 2, 10);
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).x);
			System.out.println(result.get(i).y);

		}

	}
	
	public static ArrayList<Vector2> DouglasPeucker(
			ArrayList<Vector2> points, int startIndex, int lastIndex, double epsilon) {
		double dmax = 0.0;
		int index = startIndex;
	 
		for (int i = index + 1; i < lastIndex; ++i) {
	 		double d = PointLineDistance(points.get(i), points.get(startIndex), points.get(lastIndex));
	 		if (d > dmax) {
				index = i;
				dmax = d;
			}
		}
	 
		if (dmax > epsilon) {
			ArrayList<Vector2> res1 = DouglasPeucker(points, startIndex, index, epsilon);
			ArrayList<Vector2> res2 = DouglasPeucker(points, index, lastIndex, epsilon);
	 
			ArrayList<Vector2> finalRes = new ArrayList<Vector2>();
			for (int i = 0; i < res1.size()-1; ++i) {
				finalRes.add(res1.get(i));
			}
	 
			for (int i = 0; i < res2.size(); ++i) {
				finalRes.add(res2.get(i));
			}
	 
			return finalRes;
		}
		else {
			ArrayList<Vector2> result = new ArrayList<Vector2>();
			result.add(points.get(startIndex));
			result.add(points.get(lastIndex));
			return result;
		}
	}
	 
	public static double PointLineDistance(Vector2 point, Vector2 start, Vector2 end) {
		if (start == end) {
			return point.distance(start);
		}
		
		double n = Math.abs((end.x - start.x) * (start.y - point.y) - (start.x - point.x) * (end.y - start.y));
		double d = Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y));
		
		return n / d;
	}

}
