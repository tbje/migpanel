/* Copyright 2010 Trond Bjerkestrand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.		
 */

package no.trondbjerkestrand.migpanel

import scala.swing.{ Panel, Component, LayoutContainer }
import net.miginfocom.swing.MigLayout

package object constraints {
  implicit def toUnitValue(amount: Int) = new UnitValue(amount, "px") // default to px
  implicit def toRawConstraint(x: String): FullComponentConstraint = RawConstraint(x)
	implicit def toWrap(x: Wrap.type) = Wrap()	
	implicit def toSkip(x: Skip.type) = Skip()	

  trait ComponentConstraint {
    val value: String
    override def toString = ""
  }

  object Empty extends ComponentConstraint { val value = "" }

  trait FullComponentConstraint extends ComponentConstraint {
    var next: ComponentConstraint = Empty
    def >>(insert: ComponentConstraint): FullComponentConstraint = {
      this.next match {
        case Empty => this.next = insert
        case next : FullComponentConstraint => next >> insert
      }
      this
    }
    override def toString = (next match {
				case Empty => value
				case next: FullComponentConstraint => value + ", "
	    }) + next.toString
  }
 
  object Wrap {
    def >>(next: FullComponentConstraint) = Wrap() >> next
    def apply() = new FullComponentConstraint { val value = "wrap" }
    def apply(x: Int) = new FullComponentConstraint { val value = "wrap " + x }
  }
  object Span { def apply(x: Int) = new FullComponentConstraint { val value = "span " + x } }
  object Skip { def apply(x: Int) = new FullComponentConstraint { val value = "skip " + x } }
  object GapTop { def apply(x: UnitValue) = new FullComponentConstraint { val value = "gaptop " + x } }
  object GapBottom { def apply(x: UnitValue) = new FullComponentConstraint { val value = "gapbottom " + x } }
  object GapLeft { def apply(x: UnitValue) = new FullComponentConstraint { val value = "gapleft " + x } }
  object GapRight { def apply(x: UnitValue) = new FullComponentConstraint { val value = "gapright " + x } }
 	object AlignX { 
		def apply(x: ConstrainUnit) = new FullComponentConstraint { val value = "alignx " + x }
		def leading = new FullComponentConstraint { val value = "alignx leading" } 
		def trailing = new FullComponentConstraint { val value = "alignx trailing" } 
	}
 	object AlignY { 
		def apply(x: ConstrainUnit) = new FullComponentConstraint { val value = "aligny " + x }
		def top = new FullComponentConstraint { val value = "aligny top" } 
		def bottom = new FullComponentConstraint { val value = "aligny bottom" } 
	}

  object RawConstraint {
    def apply(s: String) = new FullComponentConstraint { val value = s }
  }
 
  class UnitValue(amount: Int, unit: String = "px") {
    override def toString = amount + "" + unit
		def px = this
		def lp = new UnitValue(amount, "lp")
		def pt = new UnitValue(amount, "pt")
		def mm = new UnitValue(amount, "mm")
		def cm = new UnitValue(amount, "cm")
		def in = new UnitValue(amount, "in")
		def al = new UnitValue(amount, "al")
  }
}

import constraints._

class MigPanel(
  val layoutConstraints: String = "",
  val columnConstraints: String = "",
  val rowConstraints: String = "") extends Panel with LayoutContainer {
  override lazy val peer = {
    val mig = new MigLayout(
      layoutConstraints,
      columnConstraints,
      rowConstraints
      )
    new javax.swing.JPanel(mig) with SuperMixin
  }
  type Constraints = ComponentConstraint

  private def layoutManager = peer.getLayout.asInstanceOf[MigLayout]

  protected def constraintsFor(comp: Component): Constraints =
    layoutManager.getComponentConstraints(comp.peer).asInstanceOf[ComponentConstraint]

  protected def areValid(constr: Constraints): (Boolean, String) = (true, "")

  def add(comp: Component, constr: mig.ComponentConstraint = mig.Empty): Unit = peer.add(comp.peer, constr.toString)
}
