package it.unibo.ai.didattica.competition.tablut.IA;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.client.TablutRandomClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.GameModernTablut;
import it.unibo.ai.didattica.competition.tablut.domain.GameTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateBrandub;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class TablutTestClient extends TablutClient {
	private int game;

	public TablutTestClient(String player, String name, int gameChosen) throws IOException {
		super(player, name);
		game = gameChosen;
	}

	public TablutTestClient(String player) throws IOException {
		this(player, "test", 4);
	}

	public TablutTestClient(String player, String name) throws IOException {
		this(player, name, 4);
	}

	public TablutTestClient(String player, int gameChosen) throws IOException {
		this(player, "test", gameChosen);
	}

	public static void main(String[] args) throws IOException {
		int gametype = 4;
		String role = "";
		String name = "test";
		// TODO: change the behavior?
		if (args.length < 1) {
			System.out.println("You must specify which player you are (WHITE or BLACK)");
			System.exit(-1);
		} else {
			System.out.println(args[0]);
			role = (args[0]);
		}
		if (args.length == 2) {
			System.out.println(args[1]);
			gametype = Integer.parseInt(args[1]);
		}
		if (args.length == 3) {
			name = args[2];
		}
		System.out.println("Selected client: " + args[0]);

		TablutTestClient client = new TablutTestClient(role, name, gametype);
		client.run();
	}

	@Override
	public void run() {

		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		State state;

		Game rules = null;
		switch (this.game) {
		case 1:
			state = new StateTablut();
			rules = new GameTablut();
			break;
		case 2:
			state = new StateTablut();
			rules = new GameModernTablut();
			break;
		case 3:
			state = new StateBrandub();
			rules = new GameTablut();
			break;
		case 4:
			state = new StateTablut();
			state.setTurn(State.Turn.WHITE);
			rules = new GameAshtonTablut(99, 0, "garbage", "fake", "fake");
			System.out.println("Ashton Tablut game");
			break;
		default:
			System.out.println("Error in game selection");
			System.exit(4);
		}

		List<int[]> pawns = new ArrayList<int[]>();
		List<int[]> empty = new ArrayList<int[]>();

		System.out.println("You are player " + this.getPlayer().toString() + "!");

		while (true) {
			try {
				this.read();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(1);
			}
			System.out.println("Current state:");
			state = this.getCurrentState();
			System.out.println(state.toString());

			if (this.getPlayer().equals(Turn.WHITE)) {
				// è il mio turno (BIANCO)
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
					int[] buf;
					for (int i = 0; i < state.getBoard().length; i++) {
						for (int j = 0; j < state.getBoard().length; j++) {
							if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
									|| state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
								buf = new int[2];
								buf[0] = i;
								buf[1] = j;
								pawns.add(buf);
							} else if (state.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) {
								buf = new int[2];
								buf[0] = i;
								buf[1] = j;
								empty.add(buf);
							}
						}
					}

					int[] selected = null;

					boolean found = false;
					MinMaxEvaluate minmax = new MinMaxEvaluate(this.getCurrentState(), -1.0, 1.0, 30);
					Action a = minmax.makeDecision(this.getCurrentState());
//					try {
//						a = new Action("z0", "z0", State.Turn.WHITE);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					while (!found) {
//						if (pawns.size() > 1) {
//							selected = pawns.get(new Random().nextInt(pawns.size() - 1));
//						} else {
//							selected = pawns.get(0);
//						}
//
//						String from = this.getCurrentState().getBox(selected[0], selected[1]);
//
//						selected = empty.get(new Random().nextInt(empty.size() - 1));
//						String to = this.getCurrentState().getBox(selected[0], selected[1]);
//
//						try {
//							a = new Action(from, to, State.Turn.WHITE);
//						} catch (IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//
//						try {
//							rules.checkMove(state, a);
//							found = true;
//						} catch (Exception e) {
//
//						}
//
//					}

					System.out.println("Mossa scelta: " + a.toString());
					try {
						this.write(a);
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pawns.clear();
					empty.clear();

				}
				// � il turno dell'avversario
				else if (state.getTurn().equals(StateTablut.Turn.BLACK)) {
					System.out.println("Waiting for your opponent move... ");
				}
				// ho vinto
				else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					System.out.println("YOU WIN!");
					System.exit(0);
				}
				// ho perso
				else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("YOU LOSE!");
					System.exit(0);
				}
				// pareggio
				else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("DRAW!");
					System.exit(0);
				}

			} else {

				// � il mio turno (NERO)
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
					int[] buf;
					for (int i = 0; i < state.getBoard().length; i++) {
						for (int j = 0; j < state.getBoard().length; j++) {
							if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
								buf = new int[2];
								buf[0] = i;
								buf[1] = j;
								pawns.add(buf);
							} else if (state.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) {
								buf = new int[2];
								buf[0] = i;
								buf[1] = j;
								empty.add(buf);
							}
						}
					}

					int[] selected = null;

					boolean found = false;
					Action a = null;
					try {
						a = new Action("z0", "z0", State.Turn.BLACK);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					;
					while (!found) {
						selected = pawns.get(new Random().nextInt(pawns.size() - 1));
						String from = this.getCurrentState().getBox(selected[0], selected[1]);

						selected = empty.get(new Random().nextInt(empty.size() - 1));
						String to = this.getCurrentState().getBox(selected[0], selected[1]);

						try {
							a = new Action(from, to, State.Turn.BLACK);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						System.out.println("try: " + a.toString());
						try {
							rules.checkMove(state, a);
							found = true;
						} catch (Exception e) {

						}

					}

					System.out.println("Mossa scelta: " + a.toString());
					try {
						this.write(a);
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pawns.clear();
					empty.clear();

				}

				else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
					System.out.println("Waiting for your opponent move... ");
				} else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					System.out.println("YOU LOSE!");
					System.exit(0);
				} else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("YOU WIN!");
					System.exit(0);
				} else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("DRAW!");
					System.exit(0);
				}

			}
		}

	}

}