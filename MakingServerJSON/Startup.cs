using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Persistence.Contexts;
using WebApplication1.API.Persistence.Repositories;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Services;
using Microsoft.OpenApi.Models;
using AutoMapper;

namespace WebApplication1.API
{
    public class Startup
    {
        public IConfiguration Configuration { get; }

        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public void ConfigureServices(IServiceCollection services)
        {
            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_3_0);

            services.AddScoped<IDispenserRepository, DispenserRepository>();
            services.AddScoped<IDispenserService, DispenserService>();
            services.AddScoped<IDispRepository, DispRepository>();
            services.AddScoped<IDispService, DispService>();
            services.AddScoped<IAndroidRepository, AndroidRepository>();
            services.AddScoped<IAndroidService, AndroidService>();
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "My API", Version = "v1" });
            });
            services.AddMvc(option => option.EnableEndpointRouting = false);
            services.AddAutoMapper(typeof(Startup));
            services.AddScoped<IUnitOfWork, UnitOfWork>();
            services.AddDbContext<AppDbContext>(options => options.UseSqlServer(Configuration.GetConnectionString("DispDatabaseConnection")));

        }

        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            app.UseSwagger();

            app.UseSwaggerUI(c =>
            {
                c.SwaggerEndpoint("/swagger/v1/swagger.json", "My API V1");
            });

            if (env.IsDevelopment())
                app.UseDeveloperExceptionPage();
            else
                app.UseHsts();

            app.UseHttpsRedirection();
            app.UseMvc();
        }
    }
}