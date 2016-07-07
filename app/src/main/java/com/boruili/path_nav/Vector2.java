package com.boruili.path_nav;

public class Vector2 {
		public double x;
		public double y;
		
		public Vector2(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public double distance(Vector2 other) {
			return Math.abs(Math.sqrt(Math.pow(this.x-other.x, 2) + Math.pow(this.y - other.y, 2)));
		}
		
	}