#ifndef SIGAR_OS_H
#define SIGAR_OS_H

#include <sys/pstat.h>
#include <sys/mib.h>
#include <stdlib.h>
#include <fcntl.h>

struct sigar_t {
    SIGAR_T_BASE;
    struct pst_static pstatic;
    struct pst_status *proctab;
    time_t last_getprocs;
    sigar_pid_t last_pid;
    struct pst_status *pinfo;
};

int hpux_get_mib_ifentry(int ppa, mib_ifEntry *mib);

#endif /* SIGAR_OS_H */
