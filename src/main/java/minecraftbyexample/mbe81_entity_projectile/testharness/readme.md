The classes in this directory are only used for testing mbe81b, you can safely ignore them.  The code for mbe81b is sufficiently complex that I have created a number of automated tests to make sure it all works correctly, rather than manually testing it.

How to execute the tests:<br>
use custom command /mbedebug test TEST_NUMBER<br>
eg /mbedebug test 35<br>

Tests:
8101 = test the BoomerangFlightPath class
8102 = render a stationary boomerang to allow tweaking of yaw,pitch, and endoverendrotation
...
etc
...
8130 = collide with an invulnerable enemy (bounce off)

A number of DebugSettings are also used to assist debugging
/mbedebug param mbe81b_notick 1   = stop the boomerang tick (freezes in place)
/mbedebug param mbe81b_not_in_flight 1 = force the boomerang to non-flight mode (ballistic trajectory)
/mbedebug param mbe81b_yaw 1 = force the boomerang yaw to the given value
/mbedebug param mbe81b_pitch 1 = force the boomerang pitch to the given value
/mbedebug param mbe81b_endoverendrotation 1 = force the boomerang endoverendrotation to the given value
