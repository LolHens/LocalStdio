package org.lolhens.localstdio

import java.io.{InputStream, OutputStream, PrintStream}

object LocalStdio {
  val globalIn: InputStream = System.in
  val globalOut: PrintStream = System.out
  val globalErr: PrintStream = System.err

  private def InheritableThreadLocal[T](_initialValue: => T): InheritableThreadLocal[T] =
    new InheritableThreadLocal[T] {
      override def initialValue(): T = _initialValue
    }

  private val threadLocalIn = InheritableThreadLocal[InputStream](globalIn)
  private val threadLocalOut = InheritableThreadLocal[OutputStream](globalOut)
  private val threadLocalErr = InheritableThreadLocal[OutputStream](globalErr)

  class InputStreamProxy private[LocalStdio](inputStream: => InputStream) extends InputStream {
    override def read(): Int = inputStream.read()
    override def read(bytes: Array[Byte]): Int = inputStream.read(bytes)
    override def read(bytes: Array[Byte], i: Int, i1: Int): Int = inputStream.read(bytes, i, i1)
    override def mark(i: Int): Unit = inputStream.mark(i)
    override def markSupported(): Boolean = inputStream.markSupported()
    override def available(): Int = inputStream.available()
    override def skip(l: Long): Long = inputStream.skip(l)
    override def reset(): Unit = inputStream.reset()
    override def close(): Unit = inputStream.close()
  }

  class OutputStreamProxy private[LocalStdio](outputStream: => OutputStream) extends OutputStream {
    override def write(i: Int): Unit = outputStream.write(i)
    override def write(bytes: Array[Byte]): Unit = outputStream.write(bytes)
    override def write(bytes: Array[Byte], i: Int, i1: Int): Unit = outputStream.write(bytes, i, i1)
    override def flush(): Unit = outputStream.flush()
    override def close(): Unit = outputStream.close()
  }

  object Ignore extends OutputStream {
    override def write(i: Int): Unit = ()
    override def write(bytes: Array[Byte]): Unit = ()
    override def write(bytes: Array[Byte], i: Int, i1: Int): Unit = ()
    override def flush(): Unit = ()
    override def close(): Unit = ()
  }

  val localIn: InputStream = new InputStreamProxy(threadLocalIn.get())
  val localOut: PrintStream = new PrintStream(new OutputStreamProxy(threadLocalOut.get()))
  val localErr: PrintStream = new PrintStream(new OutputStreamProxy(threadLocalErr.get()))

  System.setIn(localIn)
  System.setOut(localOut)
  System.setErr(localErr)

  private def redirectIn(in: Option[InputStream]): Unit = in match {
    case Some(inputStream) => threadLocalIn.set(inputStream)
    case None => threadLocalIn.remove()
  }

  private def redirectOut(out: Option[OutputStream]): Unit = out match {
    case Some(outputStream) => threadLocalOut.set(outputStream)
    case None => threadLocalOut.remove()
  }

  private def redirectErr(err: Option[OutputStream]): Unit = err match {
    case Some(outputStream) => threadLocalErr.set(outputStream)
    case None => threadLocalErr.remove()
  }

  def apply[T](in: Option[InputStream] = None,
               out: Option[OutputStream] = None,
               err: Option[OutputStream] = None)(f: => T): T = {
    redirectIn(in)
    redirectOut(out)
    redirectErr(err)

    val result = f

    redirectIn(None)
    redirectOut(None)
    redirectErr(None)

    result
  }
}
