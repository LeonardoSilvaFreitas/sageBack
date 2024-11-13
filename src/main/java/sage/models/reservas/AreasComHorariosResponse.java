package sage.models.reservas;

import java.util.Arrays;
import java.util.List;

public class AreasComHorariosResponse {
    private List<Area> areas;
    private List<String> horarios;

    public AreasComHorariosResponse(List<Area> areas, List<String> horarios) {
        this.areas = areas;
        this.horarios = horarios;
    }

    // Getters e Setters
    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    public List<Area> getLocais(){
        Area auditorio = new Area("Auditório", Arrays.asList(
                new Sala("Audit 8º(823 - TS) (130)"),
                new Sala("Audit 9º (913 - TN) (60)"),
                new Sala("Audit 9º (923 - TS) (140)")
        ));
        Area labInfTN = new Area("Laboratório de Informática TN", Arrays.asList(
                new Sala("Lab 207 (23)"),
                new Sala("Lab 208 (26)"),
                new Sala("Lab 213 (40)")
        ));
        Area labInfTS = new Area("Laboratório de Informática TS", Arrays.asList(
                new Sala("1002 (25)"),
                new Sala("1003 - Acesso Livre Estud (17)"),
                new Sala("803 (30)"),
                new Sala("819 (16)"),
                new Sala("902 (25)"),
                new Sala("918 (16)")
        ));
        Area salasTN = new Area("Salas Torre Norte", Arrays.asList(
                new Sala("206 (41)"),
                new Sala("209 (32)"),
                new Sala("210 (31)"),
                new Sala("211 (35)"),
                new Sala("212 (52)"),
                new Sala("214 (30)"),
                new Sala("215 (30)"),
                new Sala("329 (30)"),
                new Sala("330 (26)"),
                new Sala("331 (25)"),
                new Sala("332 (28)"),
                new Sala("333 (20)"),
                new Sala("504 (20)")
        ));
        Area salasReuniao = new Area("Salas de Reunião", Arrays.asList(
                new Sala("Sala de Convenções 9º - C (15)"),
                new Sala("Salas de Reuniões - 907 TN (12)"),
                new Sala("Salas de Reuniões - 8ºAnd (11)")
        ));
        Area salasTS = new Area("Salas Torre Sul", Arrays.asList(
                new Sala("100 (25) Interditada"),
                new Sala("1012 (38)"),
                new Sala("1014 (32)"),
                new Sala("1015 (33)"),
                new Sala("1016 (43)"),
                new Sala("1018 (35)"),
                new Sala("1022 (25)"),
                new Sala("807 (16)"),
                new Sala("810 (10)"),
                new Sala("812 (10)"),
                new Sala("814 (35)"),
                new Sala("816 (29)"),
                new Sala("817 (30)"),
                new Sala("818"),
                new Sala("820 (27)"),
                new Sala("821 (5)"),
                new Sala("823 (10)"),
                new Sala("903 (27)"),
                new Sala("904 (30)"),
                new Sala("913 (32)"),
                new Sala("915 (30)"),
                new Sala("916 (29)"),
                new Sala("919 (29)"),
                new Sala("920 (28)"),
                new Sala("921 (21)"),
                new Sala("504 (20)")
        ));
        return Arrays.asList(auditorio, labInfTN, labInfTS, salasTN, salasReuniao, salasTS);
    }

}
