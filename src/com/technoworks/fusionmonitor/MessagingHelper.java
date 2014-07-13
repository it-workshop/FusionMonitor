package com.technoworks.fusionmonitor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Всеволод on 13.07.2014.
 */
public class MessagingHelper
{
    public static Map<Integer, FieldDouble> DOUBLES;
    public static Map<Integer, FieldVector> VECTORS;
    public static Map<Integer, FieldPID> PIDS;

    public static void init()
    {
        DOUBLES = new HashMap<Integer, FieldDouble>();

        DOUBLES.put(21, new FieldDouble("Torques.x", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getTorques().getX();
            }
        }));
        DOUBLES.put(22, new FieldDouble("Torques.y", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getTorques().getY();
            }
        }));
        DOUBLES.put(23, new FieldDouble("Torques.z", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getTorques().getZ();
            }
        }));

        DOUBLES.put(31, new FieldDouble("Angular velocity.x", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getAngularVelocity().getX();
            }
        }));
        DOUBLES.put(32, new FieldDouble("Angular velocity.y", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getAngularVelocity().getY();
            }
        }));
        DOUBLES.put(33, new FieldDouble("Angular velocity.z", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getAngularVelocity().getZ();
            }
        }));

        DOUBLES.put(41, new FieldDouble("PID values x.p", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidX().getP();
            }
        }));
        DOUBLES.put(42, new FieldDouble("PID values x.i", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidX().getI();
            }
        }));
        DOUBLES.put(43, new FieldDouble("PID values x.d", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidX().getD();
            }
        }));

        DOUBLES.put(51, new FieldDouble("PID values y.p", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidY().getP();
            }
        }));
        DOUBLES.put(52, new FieldDouble("PID values y.i", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidY().getI();
            }
        }));
        DOUBLES.put(53, new FieldDouble("PID values y.d", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidY().getD();
            }
        }));

        DOUBLES.put(61, new FieldDouble("PID values z.p", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidZ().getP();
            }
        }));
        DOUBLES.put(62, new FieldDouble("PID values z.i", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidZ().getI();
            }
        }));
        DOUBLES.put(63, new FieldDouble("PID values z.d", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidZ().getD();
            }
        }));

        DOUBLES.put(70, new FieldDouble("Heading", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getHeading();
            }
        }));

        DOUBLES.put(80, new FieldDouble("Control heading", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getControlHeading();
            }
        }));

        DOUBLES.put(90, new FieldDouble("Force", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getForce();
            }
        }));

        DOUBLES.put(100, new FieldDouble("Correction.x", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getCorrectionX();
            }
        }));

        DOUBLES.put(110, new FieldDouble("Correction.y", new GetterDouble() {
            @Override
            public double get(Messaging.Telemetry telemetry)
            {
                return telemetry.getCorrectionY();
            }
        }));

        VECTORS = new HashMap<Integer, FieldVector>();

        VECTORS.put(1, new FieldVector("Torques", new GetterVector() {
            @Override
            public Messaging.Vector get(Messaging.Telemetry telemetry)
            {
                return telemetry.getTorques();
            }
        }));

        VECTORS.put(2, new FieldVector("Angular velocity", new GetterVector() {
            @Override
            public Messaging.Vector get(Messaging.Telemetry telemetry)
            {
                return telemetry.getAngularVelocity();
            }
        }));

        PIDS = new HashMap<Integer, FieldPID>();

        PIDS.put(1, new FieldPID("PID values x", new GetterPID() {
            @Override
            public Messaging.PIDValues get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidX();
            }
        }));

        PIDS.put(2, new FieldPID("PID values y", new GetterPID() {
            @Override
            public Messaging.PIDValues get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidY();
            }
        }));

        PIDS.put(3, new FieldPID("PID values z", new GetterPID() {
            @Override
            public Messaging.PIDValues get(Messaging.Telemetry telemetry)
            {
                return telemetry.getPidZ();
            }
        }));
    }

    public double getDouble(Messaging.Telemetry telemetry, int signature)
    {
        if(!DOUBLES.containsKey(signature))
            return 0;
        return DOUBLES.get(signature).mGetter.get(telemetry);
    }

    public Messaging.Vector getVector(Messaging.Telemetry telemetry, int signature)
    {
        if(!VECTORS.containsKey(signature))
            return null;
        return VECTORS.get(signature).mGetter.get(telemetry);
    }

    public Messaging.PIDValues getPID(Messaging.Telemetry telemetry, int signature)
    {
        if(!PIDS.containsKey(signature))
            return null;
        return PIDS.get(signature).mGetter.get(telemetry);
    }

    private static class FieldDouble
    {
        public String mName;
        public GetterDouble mGetter;

        public FieldDouble(String name, GetterDouble getter)
        {
            mName = name;
            mGetter = getter;
        }
    }

    private interface GetterDouble
    {
        public double get(Messaging.Telemetry telemetry);
    }

    private static class FieldVector
    {
        public String mName;
        public GetterVector mGetter;

        public FieldVector(String name, GetterVector getter)
        {
            mName = name;
            mGetter = getter;
        }
    }

    private interface GetterVector
    {
        public Messaging.Vector get(Messaging.Telemetry telemetry);
    }

    private static class FieldPID
    {
        public String mName;
        public GetterPID mGetter;

        public FieldPID(String name, GetterPID getter)
        {
            mName = name;
            mGetter = getter;
        }
    }

    private interface GetterPID
    {
        public Messaging.PIDValues get(Messaging.Telemetry telemetry);
    }
}
