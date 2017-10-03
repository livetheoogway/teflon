<h2 align="center">Teflon</h2>
<p align="center">
<a href="https://gitlab.phonepe.com/Naik/teflon/commits/master"><img alt="build status" src="https://gitlab.phonepe.com/Naik/teflon/badges/master/build.svg" /></a>
<a href="https://gitlab.phonepe.com/Naik/teflon/commits/master"><img alt="coverage report" src="https://gitlab.phonepe.com/Naik/teflon/badges/master/coverage.svg" /></a>
</p> 

#### Task Execution Framework with Little Orchestration Needed aka TEFLON 

A framework that has the following features:
- Registration of a bunch of Task Orchestration flows of execution during startup.
- Synchronous execution of a Task
- Asynchronous execution, that will schedule the Task, based on the Orchestration registered


### Maven Dependency
Use the following maven dependency for bare minimal framework:
```
<dependency>
    <groupId>com.phonepe.teflon</groupId>
    <artifactId>teflon-framework</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
Use the following maven dependency for actor based Scheduler:
```
<dependency>
    <groupId>com.phonepe.teflon</groupId>
    <artifactId>teflon-rmq-actor</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
``` 

### Use-case
You will find this framework useful if you:
1. prefer to visualize any piece of work as a <b>Task</b>
2. want to divide the <b>Task</b> into mutually exclusive components
3. Want to mix, match, and more importantly, reuse components of one Task, in another <b>Task</b>  


#### TaskDeclaration
A TaskDeclaration is composed of the following Components:
> 1. Name - The name that is going to uniquely identify the declaration. The declaration chosen while executing a <b>Task</b>, will depend on the name of the <b>Task</b> 
> 1. <b>Source</b> - A source that emits an Input / stream of Inputs
> 2. <b>Interpreter</b> - The interpreter that takes the <b>Input</b> from the <b>Source</b> and emits an Output
> 3. <b>Sink</b> - A sink that consumes the <b>Output</b>

#### Execution of a <b>Task</b>
When a task is being executed -
1. <b>Source</b>, <b>Interpreter</b>, <b>Sink</b> are initiated.
2. <b>Inputs</b> from <b>Source</b> are streamed (in batches)
3. Batches are then passed onto the <b>Interpreter</b>
4. The Interpreted elements are then passed onto the <b>Sink</b> for consumption
5. All the while, <b>Stats</b> are collected as to how many elements were processed, time taken for execution, etc. 

### Usage
##### Annotations
Define an implementation of ```Source.java```
```java
    @SourceDeclaration(emits = Integer.class)
    public class NumberStreamGenerator implements Source<Integer> {
        int i = 0, max = 10;
        @Override
        public List<Integer> getInput() throws Exception {
            if (i <= max)
                return Collections.singletonList(i++);
            return null;
        }
    }
```
Define an ```Interpreter```
```java
    @InterpreterDeclaration(takes = Integer.class, emits = String.class)
    public class IterationInterpreter implements Interpreter<Integer, String> {
        @Override
        public List<String> interpret(List<Integer> integer) {
            return integer.stream().map(k->"Iteration: " + integer).collect(Collectors.toList());
        }
    }
```
Define a ```Sink```
```java
    @SinkDeclaration(takes = String.class)
    public class ConsoleSink implements Sink<String> {
        @Override
        public void sink(List<String> item) {
            items.forEach(System.out::println);
        }
    }
```
An finally a ```TaskDeclaration```
```java
    @TaskDeclaration(
            name = "number-generator", 
            source = NumberStreamGenerator.class,
            interpreter = IterationInterpreter.class,
            sink = ConsoleSink.class,
            factoryType = FactoryType.INJECTION)
    class SomeTask implements Task {
        @Override
        public String name() {
            return "number-generator";
        }
        ...
    }
```
Build A <b>Scheduler</b> that will allow you to trigger the task  
```java
TaskScheduler taskScheduler = TaskScheduler.builder()
                               .classPath("com.teflon.task.framework.factory")
                               .injectorProvider(() -> Guice.createInjector(<your module>))
                               .build();
// run it
taskScheduler.trigger(new SomeTask());
// or schedule it
taskScheduler.schedule(new SomeTask(), new StatusConsumer(){});
taskScheduler.scheduleAtFixedRate(new SomeTask(), new StatusConsumer(){}, 0, 1, TimeUnit.SECONDS);
```

#### RabbitMQ Actor
An integration with the [Dropwizard RabbitMQ Bundle](https://github.com/santanusinha/dropwizard-rabbitmq-actors)
```java
@Singleton
@TaskDeclaration(
        name = "pdf-statement",
        source = QuerySource.class,
        interpreter = PdfDocumentCreator.class,
        sink = EmailSendSink.class,
        factoryType = FactoryType.INJECTION
)
public class StatementEngine extends TaskActor<MessageIdType, PdfQueryStatementTask> {

    @Inject
    public StatementEngine(TaskScheduler taskScheduler,
                           TeflonConfig config,
                           RMQConnection connection,
                           ObjectMapper mapper) {
        super(MessageIdType.QUERY_PDF_STATEMENT, taskScheduler, config, connection, mapper, PdfQueryStatementTask.class);
    }
}
```
Now, every new message in the RMQ, will automatically be triggered, with corresponding Sources, Interpreters, Sinks involved.<br>
Messages (ie Tasks) will get acked automatically, after the successful execution of the task.
If not, they will be rejected/sidelined accordingly

##### TODOs
- [x] Scheduled execution<br>
- [ ] Queued Execution of Tasks using distributed zookeeper queues<br>
- [ ] Typical Source implementations in separate modules (Hbase, Es, Redis, etc)<br>
- [ ] Use artifactory based paths, for Sources<br>