package spinal.lib.misc

import spinal.core._
import spinal.lib._
import spinal.lib.eda.altera.{InterruptTag, QSysify, ResetEmitterTag}
import spinal.lib.eda.{ComponentMetaTag}
import spinal.lib.bus.avalon.{AvalonMM,AvalonMMSlaveFactory}

object AvalonInterruptCtrl {
  def getAvalonMMConfig = AvalonMMSlaveFactory.getAvalonConfig(addressWidth = 3,dataWidth = 32)

  def main(args: Array[String]) {
    val report = SpinalVerilog(new AvalonInterruptCtrl()).printPruned()
    val toplevel = report.toplevel
    toplevel.io.bus addTag(ClockDomainTag(toplevel.clockDomain))
    toplevel.io.irqReceiver addTag(InterruptTag(toplevel.clockDomain))
    toplevel.io.irqSender addTag(InterruptTag(toplevel.clockDomain))
    toplevel addTag(ComponentMetaTag(
        displayName     = "SpinalHDL Interrupt Controller (Avalon)",
        author          = "SpinalHDL",
        //version         = "1.0", --> Use Spinal Version Number
        category        = "SpinalHDL/Peripherals",
        description     = "This module multiplexes multiple IRQ senders on to a single IRQ."
    ))
    QSysify(toplevel)
  }
}

class AvalonInterruptCtrl(width : Int = 32) extends Component {
  val io = new Bundle{
    val bus         = slave(AvalonMM(AvalonInterruptCtrl.getAvalonMMConfig))
    val irqReceiver = in Bits(width bits)
    val irqSender   = out Bool
  }

  val ctrl          = InterruptCtrl(width)

  io.irqSender      <> ctrl.io.pendings.orR
  io.irqReceiver    <> ctrl.io.inputs

  val busCtrl       = AvalonMMSlaveFactory(io.bus)
  val bridge        = ctrl.driveFrom(busCtrl,0)
}
