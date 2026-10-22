class Step(val name: String, val action: () -> Unit, val compensate: () -> Unit)

enum class SagaStatus { COMPLETED, COMPENSATED, FAILED }

data class SagaResult(val status: SagaStatus, val log: List<String>)

class SagaOrchestrator(private val maxRetries: Int = 2) {

    fun run(sagaId: String, steps: List<Step>): SagaResult {
        TODO()
    }
}
