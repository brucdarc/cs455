package cs455.scaling.server;

public class ThreadPoolManager {
    /*
    the pool manager should check for different jobs that need to be done.
    There should be 3 differents tasks to be done:
    In order of descending priority:
    >Handling message chains that have time expired
    >Handling message chains that have length reached
        *maybe not this last one. It might be just as fast to just have the
        * server thread do this, since if I dont that thread
        * will spin for a long time on a lot of useless keys that
        * are already in the set
        * that means the set of keys needing to be handled will always be
        * needing to be touched by the server and worker threads needing to pull
        * things out, which will cause synchronization slowdowns
    >Reading messages incomming on socketChannels
     */




}
