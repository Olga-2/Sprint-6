import ru.sber.filesystem.VFilesystem
import ru.sber.filesystem.VPath
import java.io.IOException
import java.io.PrintWriter
import java.net.ServerSocket

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
class FileServer {

    /**
     * Main entrypoint for the basic file server.
     *
     * @param socket Provided socket to accept connections on.
     * @param fs     A proxy filesystem to serve files from. See the VFilesystem
     *               class for more detailed documentation of its usage.
     * @throws IOException If an I/O error is detected on the server. This
     *                     should be a fatal error, your file server
     *                     implementation is not expected to ever throw
     *                     IOExceptions during normal operation.
     */
    @Throws(IOException::class)
    fun run(socket: ServerSocket, fs: VFilesystem) {

        /**
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */
        while (true) {


            val conn = socket.accept()

            conn.use { s ->
                val reader = s.getInputStream().bufferedReader()
                val clientRequest = reader.readLine()
                var file: String? = ""
                if (!clientRequest.startsWith("GET"))
                    getErrorPage()
                else {
                    val splitRequest = clientRequest.split(" ")
                    if (splitRequest.size < 2)
                        getErrorPage()
                    else {
                        file = fs.readFile(VPath(splitRequest[1]))
                    }
                }
                // отправляем ответ
                val writer = PrintWriter(s.getOutputStream())
                val serverResponse = if (file != null) getSuccessPage(file) else getErrorPage()
                writer.println(serverResponse)
                writer.flush()
            }
            conn.close()
        }
    }

    private fun getSuccessPage(file: String): String {
      return  "HTTP/1.0 200 OK\r\n Server: FileServer\r\n\r\n$file"
    }

    private fun getErrorPage(): String {
        return "HTTP/1.0 404 Not Found\\r\\n Server: FileServer\\r\\n\\r\\n"
    }
}