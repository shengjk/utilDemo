package xmht.java8;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author shengjk1
 * @date 2019/11/25
 */
public class Java8Demo1 {
	public static void main(String[] args) throws IOException {
		String contents = new String(Files.readAllBytes(Paths.get("/Users/iss/sourceCode/spark/sjmsDemo/sjms.iml")));
		List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));
		for (String word : words) {
			if (word.length() > 12) {
				System.out.println("list= " + word);
			}
		}
		System.out.println(words.stream().filter(w -> w.length() > 12).count());
//		for (int i = 0; i < 100; i++) {
		Stream<String> stringStream = words.stream().filter(w -> w.length() > 12).map(String::toUpperCase);
//			System.out.println("===========");
//		}
		
		Stream<String> stream = Stream.of(contents.split("[\\P{L}]+"));
		Stream<String> song = Stream.of("gently", "down", "the", "stream");
		Stream<String> stream1 = Arrays.stream(contents.split("[\\P{L}]+"));
		Stream<Object> empty = Stream.empty();
		
		Stream<String> generate = Stream.generate(() -> "Echo");
		Stream<Double> generate1 = Stream.generate(Math::random);
		
		//seed,f(seed)
		Stream<BigInteger> limit1 = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE)).limit(10);
		
		Stream<String> limit = stringStream.limit(10);
//		limit.forEach(System.out::println);
		//丢掉前面的n个元素
//		Stream<String> stringStream1 = stringStream.skip(2);
		
		//合并两个流
		//第一个流的长度不应该是无限的，否则第二个流就永远没有机会被添加到第一个流后面
		Stream.concat(limit, limit1).forEach(System.out::println);
		
		Supplier<Stream<String>> streamSupplier
				= () -> words.stream().filter(w -> w.length() > 10);
		
		Stream<String> limit2 = streamSupplier.get().limit(10);
		Stream<String> skip = streamSupplier.get().skip(2);
		Stream.concat(limit2, skip).forEach(System.out::println);
		System.out.println("============");
		streamSupplier.get().sorted(Comparator.comparing(String::length).reversed()).forEach(System.out::println);
		System.out.println("++++++++++");
		Optional<String> max = streamSupplier.get().max(String::compareToIgnoreCase);
		if (max.isPresent()) {
			System.out.println(max.get());
		}
		
		//Optional
		ArrayList<String> result = new ArrayList<>();
		max.ifPresent(result::add);
		Optional<Boolean> aBoolean = max.map(result::add);
		aBoolean.ifPresent(System.out::println);
		
		//函数仅在需要的时候才会被调用
		System.out.println(max.orElseGet(() -> System.getProperty("user.dir")));
		String s = max.orElseThrow(NoSuchElementException::new);
		System.out.println(s);
		
		//若 s==null返回 Optional.empty(),否则返回 Optional.of(s)
		Optional.ofNullable(s);
		
		Optional<Double> aDouble = inverse(100.0).flatMap(Java8Demo1::squareRoot);
		
		//聚合
		Stream<Integer> values = null;
//		Optional<Integer> sum = values.reduce(Integer::sum);
		//不返回 Optional
//		Integer sum = values.reduce(0, Integer::sum);
		
		String[] strings = streamSupplier.get().toArray(String[]::new);
		HashSet<String> collect = streamSupplier.get().collect(HashSet::new, HashSet::add, HashSet::addAll);
		for (String s1 : collect) {
			System.out.println("++++====");
			System.out.println(s1);
		}
		Set<String> collect1 = streamSupplier.get().collect(Collectors.toSet());
		TreeSet<String> collect2 = streamSupplier.get().collect(Collectors.toCollection(TreeSet::new));
		collect2.forEach(System.out::println);
		
		//字符串拼接
		String collect3 = streamSupplier.get().collect(Collectors.joining());
		System.out.println("collect3 " + collect3);
		String collect4 = streamSupplier.get().collect(Collectors.joining(","));
		System.out.println("collect3 " + collect4);
		String collect5 = streamSupplier.get().map(Object::toString).collect(Collectors.joining(","));
		System.out.println("collect3 " + collect5);
		
		//聚合为一个总和、平均值、最大值或最小值
		IntSummaryStatistics summary = streamSupplier.get().collect(Collectors.summarizingInt(String::length));
		summary.getAverage();
		summary.getMax();
		summary.getMin();
		
		
		Supplier<Stream<Locale>> streamSupplier1
				= () -> Stream.of(Locale.getAvailableLocales());
		
		//将结果收集到 Map 中
//		相同的key多个value会报错
		Map<String, String> collect6 = stream.filter(str -> (str != null && str.contains("#")))
				.map(str -> str.split("#", -1))
				.collect(Collectors.toMap(str -> str[0], str -> str[1]));
		
		
		Stream<Locale> availableLocales = Stream.of(Locale.getAvailableLocales());
//		availableLocales.peek(System.out::println);
		availableLocales.forEach(local->{
			System.out.println(local.getDisplayLanguage()+" "+local.getDisplayLanguage(local));
		});
		//相同的key多个value会报错
		Map<String, String> collect7 = streamSupplier1.get().collect(Collectors.toMap(
				local -> local.getDisplayLanguage(),
				local -> local.getDisplayLanguage(local),
				(old, newValue) -> newValue
		));
		collect7.forEach((k, v) -> System.out.println(k + " " + v));
		
		Map<String, Set<String>> collect8 = streamSupplier1.get().collect(
				Collectors.toMap(
						local -> local.getDisplayCountry(),
						local -> Collections.singleton(local.getDisplayLanguage()),
						(a, b) -> {//a和b组合
							HashSet<String> r = new HashSet<String>(a);
							r.addAll(b);
							return r;
						}));
		
		collect8.forEach((k, v) -> System.out.println(k + " " + v));
		
		TreeMap<String, String> collect9 = streamSupplier1.get().collect(Collectors.toMap(
				local -> local.getDisplayLanguage(),
				local -> local.getDisplayLanguage(local),
				(old, newValue) -> newValue,
				TreeMap::new
		));
//
		
		
		//分组和分片
		Map<String, List<Locale>> collect10 = streamSupplier1.get().collect(Collectors.groupingBy(Locale::getDisplayCountry));
		
		//当分类函数是一个 predicate 函数(即返回一个布尔值的函数)时，流元素会被分为两组列表：一组是函数会返回true的元素，另一组会返回false元素
		//这种情况下，partitioningBy比groupingBy高效
		Map<Boolean, List<Locale>> englistAndOtherLocal = streamSupplier1.get().collect(Collectors.partitioningBy(local -> local.getDisplayLanguage().contains("en")));
		englistAndOtherLocal.forEach((k,v)-> System.out.println(k+" "+v));
		List<Locale> others = englistAndOtherLocal.get(true);
		others.forEach(x-> System.out.println("local== "+x.getDisplayLanguage()));
		englistAndOtherLocal.get(false).forEach(x-> System.out.println("local== "+x.getDisplayLanguage()));
		//
		Map<String, Set<Locale>> collect11 = streamSupplier1.get().collect(Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));
		
		Map<String, Long> collect12 = streamSupplier1.get().collect(Collectors.groupingBy(Locale::getDisplayCountry, Collectors.counting()));
		//求和
//		streamSupplier1.get().collect(Collectors.groupingBy(Locale::getCountry,Collectors.summingInt());
		//mapping方法会将一个函数应用到 downstream结果上，并且需要另一个收集器来处理啊结果
		//将城市按照其所属的州进行分组。在每个州中，我们生成每个城市的名称并按照其最大长度进行聚合
//		cities.collect(Collectors.groupingBy(CIty::getState),mapping(City::getName,maxBy(Comparator.comparing(String::length))));
		
		Map<String, Set<String>> collect13 = streamSupplier1.get().collect(Collectors.groupingBy(local -> local.getDisplayCountry(),
				Collectors.mapping(local -> local.getDisplayLanguage(), Collectors.toSet())));
		collect13.forEach((k, v) -> System.out.println(k + " " + v));
//		...
		
		//原始类型流
		IntStream intStream = IntStream.of(1, 2, 3, 4, 5);
//		Arrays.stream()
		//不包含上限
		IntStream range = IntStream.range(0, 100);
		
		
		
	}
	
	public static Optional<Double> squareRoot(Double x) {
		return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
	}
	
	public static Optional<Double> inverse(Double x) {
		return x == 0 ? Optional.empty() : Optional.of(1 / x);
	}
	
	
	public static Stream<Character> characterStream(String s) {
		ArrayList<Character> result = new ArrayList<>();
		for (char c : s.toCharArray()) {
			result.add(c);
		}
		return result.stream();
	}
}
