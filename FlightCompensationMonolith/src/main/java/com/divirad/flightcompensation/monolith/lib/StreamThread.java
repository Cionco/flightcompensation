package com.divirad.flightcompensation.monolith.lib;

import java.io.PrintStream;

public class StreamThread extends Thread {

	private PrintStream out;

	public StreamThread(PrintStream out) {
		super();
		this.out = out;
	}

	public StreamThread(Runnable target, String name, PrintStream out) {
		super(target, name);
		this.out = out;
	}

	public StreamThread(Runnable target, PrintStream out) {
		super(target);
		this.out = out;
	}

	public StreamThread(String name, PrintStream out) {
		super(name);
		this.out = out;
	}

	public StreamThread(ThreadGroup group, Runnable target, String name, long stackSize, PrintStream out) {
		super(group, target, name, stackSize);
		this.out = out;
	}

	public StreamThread(ThreadGroup group, Runnable target, String name, PrintStream out) {
		super(group, target, name);
		this.out = out;
	}

	public StreamThread(ThreadGroup group, Runnable target, PrintStream out) {
		super(group, target);
		this.out = out;
	}

	public StreamThread(ThreadGroup group, String name, PrintStream out) {
		super(group, name);
		this.out = out;
	}
	
	public PrintStream getOut() {
		return out;
	}
	
	public static StreamThread currentThread() {
		return (StreamThread) Thread.currentThread();
	}
	
}
