package absyn;

import env.Env;
import types.Type;

public enum TypeName {
   VOID {
      @Override
      public Type semantic(Env env) {
         return types.VOID.T;
      }
   },

   BOOL {
      @Override
      public Type semantic(Env env) {
         return types.BOOL.T;
      }
   },

   INT {
      @Override
      public Type semantic(Env env) {
         return types.INT.T;
      }
   },

   STRING {
      @Override
      public Type semantic(Env env) {
         return types.STRING.T;
      }
   };

   public abstract Type semantic(Env env);
}
