package jauri.junior.stepchunkbased;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job jobImprimeParImpar() {
		return jobBuilderFactory
				.get("jobImprimeParImpar")
				.incrementer(new RunIdIncrementer())
				.start(stepParImpar())
				.build();
		
	}
	
	private Step stepParImpar() {
		return stepBuilderFactory
				.get("stepParImpar")
				.<Integer,String>chunk(1)
				.reader(readerContaAteDez())
				.processor(processorParImpar())
				.writer(writerImprime())
				.build();
		
	}
	
	private IteratorItemReader<Integer> readerContaAteDez() {
		return new IteratorItemReader<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	}
	
	private FunctionItemProcessor<Integer, String> processorParImpar() {
		return new FunctionItemProcessor<>(item->item % 2 == 0 
				? String.format("Item %s é número Par", item) 
						: String.format("Item %s é número Impar", item) );
	}

	private ItemWriter<String> writerImprime() {
		return items-> items.forEach(System.out::println);
	}
	

}
