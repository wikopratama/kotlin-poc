import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.serialization.json.Json

class MeasurementConsumer(
    host: String,
    private val exchangeName: String,
    private val thresholds: Thresholds
) {
    private val channel = ConnectionFactory()
        .apply { this.host = host }
        .newConnection()
        .createChannel()

    fun start() {
        channel.exchangeDeclare(exchangeName, "fanout")
        val queueName = channel.queueDeclare().queue
        channel.queueBind(queueName, exchangeName, "")
        val callback = DeliverCallback { _, delivery ->
            val measurement = Json.decodeFromString<SensorMeasurement>(String(delivery.body))
            AlarmChecker.check(measurement, thresholds)?.let { println(it) }
        }
        channel.basicConsume(queueName, true, callback) { _ -> }
    }
}
