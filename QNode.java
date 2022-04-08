class QNode {
  // This variable should ideally be declared
  // volatile. If however this variable is
  // only checked once per resume it may not
  // be necessary as a single memory access
  // would be good enough.
  volatile boolean locked = false;
}
