package simon;

import java.awt.Color;
import java.util.ArrayList;

import guiPractice.ClickableScreen;
import guiPractice.components.Action;
import guiPractice.components.TextLabel;
import guiPractice.components.Visible;

public class SimonScreenJimmy extends ClickableScreen implements Runnable {

	private TextLabel label;
	private ButtonInterfaceJimmy[] button;
	private ProgressInterfaceJimmy progress;
	private ArrayList<MoveInterfaceJimmy> sequence;

	private int roundNumber;
	private boolean acceptingInput;
	private int sequenceIndex;
	private int lastSelectedButton;

	public SimonScreenJimmy(int width, int height) {
		super(width, height);
		Thread app = new Thread(this);
		app.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		label.setText("");
		nextRound();
	}

	private void nextRound() {
		// TODO Auto-generated method stub
		acceptingInput = false;
		roundNumber++;
		sequence.add(randomMove());
		progress.setRound(roundNumber);
		progress.setSequenceSize(sequence.size());
		changeText("Simon's turn");
		label.setText("");
		playSequence();
		changeText("Your turn");
		acceptingInput = true;
		sequenceIndex = 0;
	}

	private void playSequence() {
		// TODO Auto-generated method stub
		ButtonInterfaceJimmy b = null;
		for(int i = 0; i < sequence.size(); i++) {
			if(b != null) {
				b.dim();
			}
			b = sequence.get(i).getButton();
			b.highlight();
			int sleepTime = (int)roundNumber*200;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
 		}
		b.dim();
	}

	private void changeText(String string) {
		try {
			label.setText(string);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
	}
		
	}

	@Override
	public void initAllObjects(ArrayList<Visible> viewObjects) {
		// TODO Auto-generated method stub
		addButtons();
		progress = getProgress();
		label = new TextLabel(130, 230, 300, 40, "Let's play Simon!");
		sequence = new ArrayList<MoveInterfaceJimmy>();
		// add 2 moves to start
		lastSelectedButton = -1;
		sequence.add(randomMove());
		sequence.add(randomMove());
		roundNumber = 0;
		viewObjects.add(progress);
		viewObjects.add(label);

	}

	private MoveInterfaceJimmy randomMove() {
		ButtonInterfaceJimmy b;
		while (true) {
			int randomIndex = (int) (Math.random() * button.length);
			if (randomIndex != lastSelectedButton) {
				b = button[randomIndex];
				lastSelectedButton = randomIndex;
				break;
			}
		}
		return getMove(b);
	}

	private MoveInterfaceJimmy getMove(ButtonInterfaceJimmy b) {
		// TODO Auto-generated method stub
		return null;
	}

	private ProgressInterfaceJimmy getProgress() {
		return new Progress();

	}

	private void addButtons() {
		int numberOfButtons = 3;
		Color[] color = { Color.blue, Color.red, Color.yellow };
		for (int i = 0; i < numberOfButtons; i++) {
			final ButtonInterfaceJimmy b = getAButton();
			b.setColor(color[i]);
			b.setX(100);
			b.setY(130);
			b.setAction(new Action() {
				public void act() {
					if (acceptingInput) {
						Thread blink = new Thread(new Runnable() {
							public void run() {
								b.highlight();
								try {
									Thread.sleep(800);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								b.dim();
							}

						});
						blink.start();
						if (b == sequence.get(sequenceIndex).getButton()) {
							sequenceIndex++;
						} else {
							ProgressInterfaceJimmy.gameOver();
						}
						if (sequenceIndex == sequence.size()) {
							Thread nextRound = new Thread(SimonScreenJimmy.this);
							nextRound.start();
						}
					}
				}

			});
			viewObjects.add(b);
		}

	}

	private ButtonInterfaceJimmy getAButton() {
		return new ButtonSimon();
	}

	public void gameOver() {
		progress.gameOver();
	}

}
