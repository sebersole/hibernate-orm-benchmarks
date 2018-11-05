package org.hibernate.benchmarks;

import java.lang.Object;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

@SuppressWarnings("unused")
public class BenchmarkBase_SemanticModel {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public Object simpleSelect(BenchmarkState state) {
		return getSemanticModel( "select a from Animal a", state );
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public Object simpleSelect_multiple(BenchmarkState state) {
		return getSemanticModel(
				"select a.serialNumber, a.mother, a.father, a.description, a.zoo from Animal a",
				state
		);
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public Object simpleWhere(BenchmarkState state) {
		return getSemanticModel( "select a from Animal a where a.serialNumber = '1337'", state );
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public Object simpleWhere_multiple(BenchmarkState state) {
		return getSemanticModel(
				"select a from Animal a where a.serialNumber = '1337' and a.zoo.address.city = 'London' and a.zoo.address.country = 'US'",
				state
		);
	}

	private Object getSemanticModel(String query, BenchmarkState benchmarkState) {
		return benchmarkState.getHqlSemanticInterpreter().getSemanticModel( query );
	}
}
