# reactive-app

###### operadores multithread
publishOn: ele pode usar 1 ou mais threads para executar a rotina, mas e sempre na ordem dos eventos.
<br>
subscribeOn: posso mudar de assinante no meio do fluxo.
<br>
parallel: executar fluxos de forma paralela.
<br>
scheduler (grupo de trabalhadores): é uma interface que possui 2 métodos centrais: schedule(agenda uma tarefa runnable) e createWorker(dedica a um worker, mas agenda a mesma forma.)
<br>
Implementações scheduler:
<br>
Schedulers.immediate () - o encadeamento atual
<br>
Schedulers.single () - um único thread reutilizável. Isso reutilizará o mesmo thread único até que o Agendador seja descartado.
<br>
Schedulers.newSingle () - um único thread dedicado.
<br>
Schedulers.elastic () - cria um novo pool de trabalhadores conforme necessário e reutiliza os inativos. As linhas ociosas (padrão 60s) são descartadas.
<br>
Schedulers.parallel () - você pode criar um número específico de threads para esse Scheduler. O padrão é o núcleo da CPU.
