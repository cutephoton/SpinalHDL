/*
 * SpinalHDL
 * Copyright (c) Dolu, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package spinal.test

import spinal.ImportMe._
import spinal._

/**
 * Created by PIC18F on 22.08.2014.
 */
object Try2 {

  class BundleAA extends BundleA {
    val a = new Bool()
    val d = new Bool()
    val e = MyEnum.craft()
  }

  class BundleA extends Bundle {
    val b = new Bool()
    val c = UInt(8 bit)
  }

  object MyEnum extends SpinalEnum {
    val s0, s1, s2 = Value
  }

  object MyEnum2 extends SpinalEnum {
    val e0, e1, e2 = Value
  }

  class ComponentA extends Component {
    val io = new Bundle {
      val cond0 = in.Bool()
      val cond1 = in.Bool()
      val cond2 = in.Bool()
      val cond3 = in.Bool()

      val wrEnable = in.Bool()
      val wrAddr = in UInt (4 bit)
      val wrData = in(new BundleA)

      val rdEnable = in.Bool()
      val rdAddr = in UInt (4 bit)
      val rdData = out(new BundleA)
    }


    val mem = new Mem(io.wrData, 1 << io.wrAddr.getWidth)

    when(io.cond0 && io.cond1) {
      mem.write(io.wrAddr + UInt(1), io.wrData)
    }
    val tmp = RegNext(mem.read(io.rdAddr + UInt(2)))
    io.rdData := tmp
    tmp.add(new AttributeString("myAttribut", "hallo"))
    tmp.add(new AttributeFlag("yolo"))

  }


  def main(args: Array[String]) {
    println("START")
    var comp: ComponentA = null

    SpinalMain({
      comp = new ComponentA
      Component(comp)
    })

    new VhdlTestBenchBackend().elaborate(comp)
    println("DONE")


  }

}
