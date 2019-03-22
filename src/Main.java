import java.awt.print.Printable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.FileNotFoundException;

public class Main {
	static int tagsCounter = 0;

	public static void main(String[] args) throws IOException {
		File file = new File("a_example.txt");
		Scanner scanner = new Scanner(file);
		HashMap<String, Integer> tagToId = new HashMap<>();
		int n = scanner.nextInt();
		scanner.nextLine();
		List<Image> images = new ArrayList<>();
		List<Integer> hor = new ArrayList<>();
		List<Integer> ver = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			String[] parse = scanner.nextLine().split(" ");
			Image im = new Image();
			im.id = i;
			if (parse[0].equals("H")) {
				im.type = true;
				hor.add(i);
			} else {
				im.type = false;
				ver.add(i);
			}
			for (int k = 2; k < parse.length; k++) {

				if (!tagToId.containsKey(parse[k])) {
					tagToId.put(parse[k], tagsCounter++);

				}
				im.tags.add(tagToId.get(parse[k]));

			}
			images.add(im);
		}
		State bestState = new State();

		System.out.println("begin");
	     int min = Integer.MAX_VALUE;
       int in = 0;
       for (int i = 0; i < images.size(); i++) {
           int cur = images.get(i).tags.size();
           if (cur < min) {
               min = cur;
               in = i;
           }
       }
		List<Slice> slices = new ArrayList<>();
		slices.add(new Slice(images.get(in).type, in, -1));
		slices.get(0).tags = images.get(in).tags;

		int c = 0;
		Set<Integer> used = new HashSet<>();
		used.add(in);
		while (true) {
			if(c%100 == 0)
			System.out.println(c);
			boolean f = false;
			if (slices.get(c).type || slices.get(c).image2 != -1) {
				double maxSc = 0;
				int index = -1;
				for (int i = 0; i < images.size(); i += 1) {
					if (!used.contains(i)) {
						double curScore = getSliceScore(slices.get(c).tags, images.get(i).tags);
						curScore /= Math.log(Math.log(images.get(i).tags.size()));
						if (curScore >= maxSc) {
							maxSc = curScore;
							index = i;
						}
					}
				}
				if (index != -1) {
					if (images.get(index).type) {
						slices.add(new Slice(true, index, -1));
						c++;
						slices.get(c).tags = images.get(index).tags;
						used.add(index);
					} else {
						slices.add(new Slice(false, index, -1));
						c++;
						slices.get(c).tags = images.get(index).tags;
						used.add(index);
					}

				} else {
					f = true;
				}
			}

			else {
				double maxSc = 0;
				double minSc = Integer.MAX_VALUE;
				int index = -1;
				for (int i = 0; i < images.size(); i += 1) {
					if (!used.contains(i) && !images.get(i).type) {
						List<Integer> tags = union(slices.get(c).tags, images.get(i).tags);
						double curScore = 0;
						if (c != 0) {
							curScore = getSliceScore(tags, slices.get(c - 1).tags);
							curScore /= Math.log(Math.log(images.get(i).tags.size()));
							if (curScore > maxSc) {
								maxSc = curScore;
								index = i;
							}
						} else {
							curScore = getSliceScore(images.get(i).tags, slices.get(c).tags);
							curScore *= Math.log(Math.log(images.get(i).tags.size()));
							if (curScore < minSc) {
								minSc = curScore;
								index = i;
							}
						}

					}
				}
				if (index != -1) {
					slices.get(c).image2 = index;
					for (int k = 0; k < images.get(index).tags.size(); k++) {
						if (!slices.get(c).tags.contains(images.get(index).tags.get(k))) {
							slices.get(c).tags.add(images.get(index).tags.get(k));
						}
						used.add(index);
					}
				} else {
					f = true;
				}
			}
			if (f) {
				break;
			}

		}

//		State cur = bestState;
//		int maxSc = getScore(bestState);
//		int scur = maxSc;
//		int t = 0;
//		int limit =10000;
//		while(true) {
//			t++;
//			System.out.println(t);
//			State next = change(cur);
//			int snext = getScore(next);
//			if(snext > maxSc) {
//				maxSc = snext;
//				bestState = next;
//				System.out.println("done");
//			}
//			
//			int e = snext - scur;
//			System.out.println(e);
//
//			if(t == limit)
//				break;
//			
//			if(e > 0) {
//				scur = snext;
//				cur = next;
//			}
//			System.out.println(e);
//			System.out.println("__");
//			
//			
//			
//			
//		}
//		
		bestState.state = slices;
		System.out.println(getScore(bestState));

		int totalCount = bestState.state.size();
//		System.out.println(bestState.state.size());
		PrintWriter write = new PrintWriter("out.txt");
		write.println(totalCount);

		for (int i = 0; i < bestState.state.size(); i++) {
			if (bestState.state.get(i).type) {
				write.println(bestState.state.get(i).image1);
			} else {
				write.println(bestState.state.get(i).image1 + " " + bestState.state.get(i).image2);
			}
		}
		write.close();

	}

	static State change(State state) {
		List<Slice> newList = new ArrayList<>(state.state);
		State newS = new State();
		newS.state = newList;

		Random r = new Random();
//			System.out.println(r.nextFloat());
		int r1 = r.nextInt(state.state.size() - 1);
		int r2 = r.nextInt(state.state.size() - 1);
		// System.out.println(r1 + " " + r2);
		if (r1 == r2) {
			r2 = (r1 + 1) % state.state.size();
		}
		Collections.swap(newS.state, r1, r2);
		for (int j = r1; j <= (r2 + r1) / 2; j++) {
			Collections.swap(newS.state, r1, r2 - j + r1 - 1);

		}

		return newS;

	}

	static int getScore(State state) {
		int sc = 0;

		for (int i = 0; i < state.state.size() - 1; i++) {

			int inter = intersection(state.state.get(i).tags, state.state.get(i + 1).tags).size();
			if (inter != 0) {
				System.out.println("opsssss" + inter);
			}

			sc += Math.min(Math.min(inter, state.state.get(i).tags.size() - inter),
					state.state.get(i + 1).tags.size() - inter);
		}
		return sc;
	}

	static int getSliceScore(Slice s1, Slice s2) {
		int inter = intersection(s1.tags, s2.tags).size();

		return Math.min(Math.min(inter, s1.tags.size() - inter), s2.tags.size() - inter);
	}

	static int getSliceScore(List<Integer> t1, List<Integer> t2) {
		int inter = intersection(t1, t2).size();

		return Math.min(Math.min(inter, t1.size() - inter), t2.size() - inter);
	}

	static void printAllHor(List<Integer> hor, List<Integer> ver) throws FileNotFoundException {
		int totalCount = hor.size() + ver.size();
		PrintWriter write = new PrintWriter("out.txt");
		write.println(totalCount);
		for (int i = 0; i < hor.size(); i++) {
			write.println(hor.get(i));
//	            System.out.println(i);
		}
		for (int i = 0; i < ver.size(); i++) {
//	           write.println(ver.get(i).a+" "+ver.get(i).b);
		}

		write.close();
	}

	public static int unionCount(int image1, int image2, List<Image> images) {
		List<Integer> list1 = images.get(image1).tags;
		List<Integer> list2 = images.get(image2).tags;

		return union(list1, list2).size();

	}

	public static <T> List<T> union(List<T> list1, List<T> list2) {
		Set<T> set = new HashSet<T>();

		set.addAll(list1);
		set.addAll(list2);

		return new ArrayList<T>(set);
	}

	public static <T> List<T> intersection(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();

		for (T t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}

		return list;
	}

}

class State {
	List<Slice> state;
}

class Slice {
	boolean type;
	int image1;
	int image2;
	List<Integer> tags;

	Slice(boolean type, int image1, int image2) {
		this.type = type;
		this.image1 = image1;
		this.image2 = image2;

	}

}

class Pair {
	int a;
	int b;

	Pair(int a, int b) {
		this.a = a;
		this.b = b;
	}
}

class Image {
	int id;
	boolean type;
	ArrayList<Integer> tags;

	public Image() {
		tags = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "Image{" + "type=" + type + ", tags=" + tags + '}';
	}
}

class newSort implements Comparator<Slice> {

	@Override
	public int compare(Slice o1, Slice o2) {
		// TODO Auto-generated method stub
		return o1.tags.size() - o2.tags.size();
	}

	int getSliceScore(List<Integer> t1, List<Integer> t2) {
		int inter = intersection(t1, t2).size();
//		return inter;
		return Math.min(Math.min(inter, t1.size() - inter), t2.size() - inter);
	}

	static void printAllHor(List<Integer> hor, List<Integer> ver) throws FileNotFoundException {
		int totalCount = hor.size() + ver.size();
		PrintWriter write = new PrintWriter("out.txt");
		write.println(totalCount);
		for (int i = 0; i < hor.size(); i++) {
			write.println(hor.get(i));
//	            System.out.println(i);
		}
		for (int i = 0; i < ver.size(); i++) {
//	           write.println(ver.get(i).a+" "+ver.get(i).b);
		}

		write.close();
	}

	public <T> List<T> union(List<T> list1, List<T> list2) {
		Set<T> set = new HashSet<T>();

		set.addAll(list1);
		set.addAll(list2);

		return new ArrayList<T>(set);
	}

	public <T> List<T> intersection(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();

		for (T t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}

		return list;
	}

}