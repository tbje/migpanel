##MigPanel:##
MigPanel is a thin scala wrapper for the MigLayout. Use of this wrapper allows type checking the MigLayout constraints.

### Usage: ###

		object MyPanel extends MigPanel {
				add(new Label("Hello"), Span >> AlignX(10 cm) >> GapBottom(10))
				add(new Label("MigLayout"), Skip(2) >> GapLeft(10 px))
				add(new Label("Wrapper"), Span >> AlignX(10 cm) >> GapBottom(10))
		}

### Supported constraints: ###
* Wrap
* Span
* Skip
* GapTop
* GapBottom
* GapLeft  
* GapRight 
* AlignX
* AlignY
* RawConstraint
* Empty

Specifying unsupported constraints can be done using RawConstraint. There is an implicit conversion from String to RawConstraint allowing:

		add(new Label("Hello"), Span >> "shrinkprio 50"))

### Credits: ###
Credits to [hotzen](https://github.com/hotzen) for writing the initial MigPanel which this tiny DSL was built on top of. 
